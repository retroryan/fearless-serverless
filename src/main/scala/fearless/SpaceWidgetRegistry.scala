package fearless

import java.sql.Statement
import java.util.UUID

import com.typesafe.config.Config
import com.typesafe.scalalogging.StrictLogging
import com.zaxxer.hikari.HikariDataSource

import scala.collection.concurrent._
import scala.concurrent.duration.Duration

case class SpaceWidget(id: String, description: String)


// Return types are backwards to conform to the return type of scala TrieMap
// These methods return None if successful
// Or Some(exception)
// Better would be to map these to either
trait SpaceWidgetRegistry {
  def addWidget(spaceWidget: SpaceWidget): Option[String]

  def removeWidget(id: String): Option[String]

  def all(): Iterable[SpaceWidget]
}

class DbSpaceWidgetRegistry private(dataSource: HikariDataSource) extends SpaceWidgetRegistry  with StrictLogging {

  val db: Statement = dataSource.getConnection.createStatement()

  override def addWidget(spaceWidget: SpaceWidget): Option[String] = {
    logger.info(s"Adding $spaceWidget")
    try {
      val insert_statement =
        s"""
              INSERT INTO space_widgets (id, description)
              VALUES ('${spaceWidget.id}', '${spaceWidget.description}');
            """
      logger.info(s"INSERT -> $insert_statement")
      db.executeUpdate(
        insert_statement)
      None
    }
    catch {
      case exc: Throwable =>
        logger.error(exc.toString)
        Some("Error inserting widgets into DB")
    }
  }

  override def removeWidget(id: String): Option[String] = ???

  override def all(): Iterable[SpaceWidget] = {
    val resultSet = db.executeQuery("SELECT ID, DESCRIPTION FROM space_widgets")
    var widgets = List.empty[SpaceWidget]

    while (resultSet.next()) {
      val id = resultSet.getString("id")
      val description = resultSet.getString("description")
      val nextWidget = SpaceWidget(id, description)
      widgets = widgets :+ nextWidget
      logger.debug(s"next widget -> $nextWidget")
    }
    widgets
  }
}

object DbSpaceWidgetRegistry extends StrictLogging {
  def apply(config: Config, startupDelayTime: Duration): DbSpaceWidgetRegistry = {
    val dataSource: HikariDataSource = Hikari(config)
    val registry = new DbSpaceWidgetRegistry(dataSource)

    logger.info(s"Loading Space Widget Registry from DB. Waiting ${startupDelayTime}")
    Thread.sleep(startupDelayTime.length * 1000)
    List(
      SpaceWidget(UUID.randomUUID().toString, "Space Helmet"),
      SpaceWidget(UUID.randomUUID().toString, "Moon Boots"),
      SpaceWidget(UUID.randomUUID().toString, "Jet Pack"),
      SpaceWidget(UUID.randomUUID().toString, "Moon gloves"),
      SpaceWidget(UUID.randomUUID().toString, "Heat shield")
    ).foreach { widget =>
      registry.addWidget(widget)
    }
    logger.info(s"Space Widget Registry loaded from cache.")
    registry
  }
}

class LocalSpaceWidgetRegistry extends SpaceWidgetRegistry {
  val widgetRegistry = new TrieMap[String, SpaceWidget]

  def addWidget(spaceWidget: SpaceWidget): Option[String] = {
    widgetRegistry.put(spaceWidget.id, spaceWidget).map(_ => "Widget Already Exists in Registry")
  }

  def removeWidget(id: String): Option[String] = {
    widgetRegistry.remove(id).map(_ => "Widget does not exist")
  }

  def all(): Iterable[SpaceWidget] = widgetRegistry.readOnlySnapshot().values
}

object LocalSpaceWidgetRegistry extends StrictLogging {

  def apply(startupDelayTime: Duration): LocalSpaceWidgetRegistry = {
    val registry = new LocalSpaceWidgetRegistry
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
