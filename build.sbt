name := "fearless-serverless"
version := "0.1"

scalaVersion := "2.12.10"

val AkkaVersion = "2.5.26"
val AkkaHttpVersion = "10.1.9"
val AkkaHttpCirceVersion = "1.29.1"
val CirceVersion = "0.12.0-RC1"
val ScalaLoggingVersion = "3.9.2"



lazy val root = (project in file("."))
  .enablePlugins(JavaServerAppPackaging)
  .settings(
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion,
      "de.heikoseeberger" %% "akka-http-circe" % AkkaHttpCirceVersion,
      "io.circe" %% "circe-core" % CirceVersion,
      "io.circe" %% "circe-generic" % CirceVersion,
      "com.typesafe.scala-logging" %% "scala-logging" % ScalaLoggingVersion,
      "org.slf4j" % "slf4j-simple" % "1.7.26"
    )
  )
