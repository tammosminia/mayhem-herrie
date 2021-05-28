import Messages.RegisterMessage
import akka.actor.ActorSystem

import java.net.InetSocketAddress
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object Main extends App {
  val echt = new InetSocketAddress("mayhem.jdriven.com", 1337)

  val rm = RegisterMessage("Herrie", "tammosminia@jdriven.com", "***")

  val s = ActorSystem("herrie")
  val a = s.actorOf(AkkaClient.props(echt, rm, true))
  Await.result(s.whenTerminated, Duration.Inf)
}

object MainTest extends App {
  val lokaal = new InetSocketAddress("0.0.0.0", 1337)
  val echt = new InetSocketAddress("mayhem.jdriven.com", 1337)

  val rm = RegisterMessage("Herrie", "tammosminia@jdriven.com", "***")
  val rm2 = RegisterMessage("Herrie2", "tammosminia2@jdriven.com", "***")

  val s = ActorSystem("herrie")
  val a = s.actorOf(AkkaClient.props(lokaal, rm, true))
  val a2 = s.actorOf(AkkaClient.props(lokaal, rm2, false))
  Await.result(s.whenTerminated, Duration.Inf)
}
