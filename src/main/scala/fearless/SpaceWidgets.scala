package fearless

import java.util.UUID

import com.typesafe.scalalogging.StrictLogging

import scala.collection.concurrent._
import scala.concurrent.duration.Duration

case class SpaceWidget(id: String, description: String)

class SpaceWidgetRegistry {
  val widgetRegistry = new TrieMap[String, SpaceWidget]

  def addWidget(spaceWidget: SpaceWidget): Option[SpaceWidget] = {
    widgetRegistry.put(spaceWidget.id, spaceWidget)
  }

  def removeWidget(id: String): Option[SpaceWidget] = {
    widgetRegistry.remove(id)
  }

  def all(): Iterable[SpaceWidget] = widgetRegistry.readOnlySnapshot().values
}

object SpaceWidgetRegistry extends StrictLogging {

  def apply(startupDelayTime:Duration): SpaceWidgetRegistry = {
    val registry = new SpaceWidgetRegistry


    logger.info(s"Loading Space Widget Registry from cache. Waiting ${startupDelayTime}")

    Thread.sleep(startupDelayTime.length * 1000)

    List(
      SpaceWidget(UUID.randomUUID().toString, "Space Helmet"),
      SpaceWidget(UUID.randomUUID().toString, "Moon Boots"),
      SpaceWidget(UUID.randomUUID().toString, "Jet Pack")
    ).foreach { widget =>
      registry.addWidget(widget)
    }

    logger.info(s"Space Widget Registry loaded from cache.")

    registry
  }


}
