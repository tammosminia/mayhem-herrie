import Fight.actionForStatus
import Messages.{ErrorMessage, RegisterMessage, StatusMessage, WelcomeMessage}
import akka.actor.{Actor, ActorRef, Props}
import akka.io.Tcp._
import akka.io.{IO, Tcp}
import akka.util.{ByteString, CompactByteString}
import spray.json._

import java.net.InetSocketAddress
import scala.util.Try

object AkkaClient {
  def props(remote: InetSocketAddress, rm: RegisterMessage, noisy: Boolean) = Props(classOf[AkkaClient], remote, rm, noisy)

}

class AkkaClient(remote: InetSocketAddress, rm: RegisterMessage, noisy: Boolean) extends Actor {
  import context.system
  IO(Tcp) ! Connect(remote)

  def log(s: String) = {
    if (noisy) println(s)
  }

  def write[M: JsonWriter](connection: ActorRef, m: M) = {
    val s = m.toJson.compactPrint
    log(s"writing $s")
    connection ! Write(CompactByteString(s.appended('\n').getBytes))
  }

  def receive = {
    case CommandFailed(c: Connect) =>
      println("connect failed")
      println(c)
      context.stop(self)

    case c @ Connected(remote, local) =>
      println(c)
      val connection = sender()
      connection ! Register(self)

      context.become {
        case data: ByteString =>
          connection ! Write(data)
        case CommandFailed(w: Write) =>
          // O/S buffer was full
          println("write failed")
        case Received(data: ByteString) =>
          val s = data.utf8String
          println(s"received $s")
          Try(s.parseJson.convertTo[WelcomeMessage])
            .map { m =>
              println("got welcome, now registering")
              write(connection, rm)
              context.become(running(connection))
            }
        case "close" =>
          connection ! Close
        case _: ConnectionClosed =>
          println("connection closed")
          context.stop(self)
      }
  }

  def running(connection: ActorRef): Actor.Receive = {
    case Received(data: ByteString) =>
      val s = data.utf8String
      log(s"running received $s")
      Try(s.parseJson.convertTo[StatusMessage]).map { status =>
        actionForStatus(status).foreach { a =>
          write(connection, a)
        }
      }
      Try(s.parseJson.convertTo[ErrorMessage]).map { e =>
        println(s"problem. exit! $e")
        system.terminate()
      }

  }
}
