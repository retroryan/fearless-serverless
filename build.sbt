name := "fearless-serverless"

scalaVersion := "2.12.10"

val AkkaVersion                     = "2.5.26"
val AkkaHttpVersion                 = "10.1.9"
val AkkaHttpCirceVersion            = "1.29.1"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion,
  "de.heikoseeberger" %% "akka-http-circe" % AkkaHttpCirceVersion,
  "org.slf4j" % "slf4j-simple" % "1.7.26"
)
