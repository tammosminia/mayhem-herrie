import Messages.RegisterMessage
import zio.console._
//import zhttp.http.HttpData
//import zhttp.service.{ChannelFactory, Client, EventLoopGroup}
import zio._
import sttp.client3._
//import Messages._

object Main extends zio.App {
  def run(args: List[String]) =
    mayhem.exitCode

  val mayhem =
    for {
      _ <- putStrLn("Mayhem!")
      r <- Api.call
      _ <- putStrLn(s"r:${r}")
    } yield ()
}

//@main def mayhem: Unit =
//  println("Mayhem!")
//  println(msg)

object Api {
  val url = "http://0.0.0.0:1337"
  val registerMessage = RegisterMessage("Herrie", "tammosminia@jdriven.com", "***")

  //sttp
  val backend = HttpURLConnectionBackend()
  def call =
    Task {
      val request = basicRequest
      // send the body as form data (x-www-form-urlencoded)
      //      .body(Map("name" -> "John", "surname" -> "doe"))
      // use an optional parameter in the URI
        .body(registerMessage)
        .post(uri"$url")
      val response = request.send(backend)
      response.body
    }
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
