name := "fearless-serverless"
version := "0.3"

scalaVersion := "2.12.10"

val AkkaVersion = "2.5.26"
val AkkaHttpVersion = "10.1.9"
val AkkaHttpCirceVersion = "1.29.1"
val CirceVersion = "0.12.0-RC1"
val ScalaLoggingVersion = "3.9.2"
val HikariCPVersion = "3.3.1"
val Slf4jVersion = "1.7.26"
val PostgresqlVersion = "9.4.1208"


lazy val root = (project in file("."))
  .enablePlugins(JavaServerAppPackaging)
  .settings(
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion,
      "de.heikoseeberger" %% "akka-http-circe" % AkkaHttpCirceVersion,
      "io.circe" %% "circe-core" % CirceVersion,
      "io.circe" %% "circe-generic" % CirceVersion,
      "com.typesafe.scala-logging" %% "scala-logging" % ScalaLoggingVersion,
      "com.zaxxer" % "HikariCP" % HikariCPVersion,
      "org.postgresql" % "postgresql" %  PostgresqlVersion,
      "org.slf4j" % "slf4j-simple" % Slf4jVersion
    )
  )
