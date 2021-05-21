import Messages.RegisterMessage
import akka.actor.ActorSystem
import zio.console._

import scala.concurrent.Await
import scala.concurrent.duration.Duration
//import zhttp.http.HttpData
//import zhttp.service.{ChannelFactory, Client, EventLoopGroup}
//import zio._
import sttp.client3._
//import Messages._
import sttp.client3.sprayJson._
import spray.json._
import akka.actor.{Actor, ActorRef, Props}
import akka.io.{IO, Tcp}
import akka.util.ByteString
import java.net.InetSocketAddress

object Main extends App {
  val lokaal = new InetSocketAddress("0.0.0.0", 1337)
  val echt = new InetSocketAddress("mayhem.jdriven.com", 1337)

  val rm = RegisterMessage("Herrie", "tammosminia@jdriven.com", "***")
  val rm2 = RegisterMessage("Herrie2", "tammosminia2@jdriven.com", "***")

  val s = ActorSystem("herrie")
  val a = s.actorOf(AkkaClient.props(lokaal, rm, true))
  val a2 = s.actorOf(AkkaClient.props(lokaal, rm2, false))
  Await.result(s.whenTerminated, Duration.Inf)
}

//zio
//object Main extends zio.App {
//  def run(args: List[String]) =
//    mayhem.exitCode
//
//  val mayhem =
//    for {
//      _ <- putStrLn("Mayhem!")
//      r <- Api.call
//      _ <- putStrLn(s"r:${r}")
//    } yield ()
//}

//@main def mayhem: Unit =
//  println("Mayhem!")
//  println(msg)

object Api {
//  val url = "http://0.0.0.0:1337"

  //akka

  //sttp
//  val backend = HttpURLConnectionBackend()
//  def call =
//    Task {
//      val request = basicRequest.
//      // send the body as form data (x-www-form-urlencoded)
//      //      .body(Map("name" -> "John", "surname" -> "doe"))
//      // use an optional parameter in the URI
//        .body(registerMessage)
//        .post(uri"$url")
//      val response = request.send(backend)
//      response.body
//    }
//  zio-http
//  def call =
//    for {
//      res <- Client.request("http://0.0.0.0:8080")
//      _ <- console.putStrLn {
//        res.content match {
//          case HttpData.CompleteData(data) => data.map(_.toChar).mkString
//          case HttpData.StreamData(_)      => "<Chunked>"
//          case HttpData.Empty              => ""
//        }
//      }
//    } yield ()
}
