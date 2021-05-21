import zio.console._

object Main extends zio.App {
  def run(args: List[String]) =
    mayhem.exitCode

  val mayhem =
    for {
      _    <- putStrLn("Mayhem!")
      name <- getStrLn
      _    <- putStrLn(s"Hello, ${name}, welcome to ZIO!")
    } yield ()
}

//@main def mayhem: Unit =
//  println("Mayhem!")
//  println(msg)
