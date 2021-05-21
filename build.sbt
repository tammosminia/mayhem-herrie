val scala3Version = "3.0.0"

lazy val root = project
  .in(file("."))
  .settings(
    name := "scala3-simple",
    version := "0.1.0",
    scalaVersion := scala3Version,
    libraryDependencies += "com.novocode" % "junit-interface" % "0.11" % "test",
    libraryDependencies += "dev.zio" %% "zio" % "1.0.8",
//    libraryDependencies += "io.d11" % "zhttp_3.0.0-RC3" % "1.0.0.0-RC16"
    libraryDependencies += "com.softwaremill.sttp.client3" %% "core" % "3.3.4",
    libraryDependencies += "com.softwaremill.sttp.client3" %% "spray-json" % "3.3.4"
  )
