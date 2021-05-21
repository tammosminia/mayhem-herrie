val AkkaVersion = "2.6.14"

lazy val root = project
  .in(file("."))
  .settings(
    name := "mayhem-herrie",
    version := "0.1.0",
    scalaVersion := "2.13.6",
    libraryDependencies += "dev.zio" %% "zio" % "1.0.8",
//    libraryDependencies += "io.d11" % "zhttp_3.0.0-RC3" % "1.0.0.0-RC16"
    libraryDependencies += "com.softwaremill.sttp.client3" %% "core" % "3.3.4",
    libraryDependencies += "com.softwaremill.sttp.client3" %% "spray-json" % "3.3.4",
    libraryDependencies += "com.typesafe.akka" %% "akka-actor" % AkkaVersion
  )
