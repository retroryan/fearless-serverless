package fearless

import java.util.Properties

import com.typesafe.config.Config
import com.typesafe.scalalogging.StrictLogging
import com.zaxxer.hikari.{HikariConfig, HikariDataSource}


object Hikari extends StrictLogging {

  def apply(config: Config): HikariDataSource = {
    val props = new Properties()
    props.setProperty("dataSourceClassName", "org.postgresql.ds.PGSimpleDataSource")
    props.setProperty("dataSource.user", config.getString("ctx.dataSource.user"))
    props.setProperty("dataSource.password", config.getString("ctx.dataSource.password"))
    props.setProperty("dataSource.databaseName", config.getString("ctx.dataSource.dbName"))
    props.setProperty("dataSource.portNumber", config.getString("ctx.dataSource.dbPort"))
    props.setProperty("dataSource.serverName", config.getString("ctx.dataSource.dbHost"))
    logger.info(s"connecting with db props: $props")
    new HikariDataSource(new HikariConfig(props))
  }

}
