package fearless

import java.util.concurrent.TimeUnit

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.typesafe.config.{Config, ConfigFactory}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContextExecutor, Future}
import scala.io.StdIn

object WebServer {
  def main(args: Array[String]): Unit = {
    println("Started Fearless!")

    implicit val actorSystem = ActorSystem("Fearless")

    new WebServer()


  }
}

class WebServer()(implicit val system: ActorSystem) extends WebRoutes {

  lazy val routes: Route = webRoutes
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher


  val config: Config = ConfigFactory.load()
  val host: String = config.getString("fearless.server.host")
  val port: Int = config.getInt("fearless.server.port")
  val startupDelay: Long = config.getDuration("fearless.server.startup.delay", TimeUnit.SECONDS)

  private val duration = Duration(startupDelay, TimeUnit.SECONDS)
  println(s"$startupDelay   and $duration")

  override val registry: SpaceWidgetRegistry = SpaceWidgetRegistry(duration)

  val bindingFuture: Future[Http.ServerBinding] =
    Http().bindAndHandle(routes, host, port)

  println(s"Server online at http://$host:$port/")

  while (!Thread.currentThread.isInterrupted) {}


  bindingFuture
    .flatMap(_.unbind()) // trigger unbinding from the port
    .onComplete(_ => system.terminate()) // and shutdown when done

  Await.ready(system.whenTerminated, Duration.Inf)

}
