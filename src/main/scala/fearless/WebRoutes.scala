package fearless

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.generic.auto._

trait WebRoutes {

  implicit def system: ActorSystem

  val registry: SpaceWidgetRegistry

  lazy val log = Logging(system, classOf[WebRoutes])

  lazy val webRoutes: Route = {

    pathPrefix("widgets") {
      pathEnd {
        get {
          complete(registry.all())
        }
      }
    } ~
      pathPrefix("add") {
        post {
          entity(as[SpaceWidget]) { spaceWidget =>
            val maybeWidget = registry.addWidget(spaceWidget)
            println(s"maybe: $maybeWidget")
            registry.all().foreach(println)

            maybeWidget match {
              case None => complete(StatusCodes.Accepted, "Added Widget to Registry")
              case Some(exc) => complete(StatusCodes.Conflict, exc)
            }
          }
        }
      }
  }
}
