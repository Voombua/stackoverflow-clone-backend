package com.voombua.utils

import org.flywaydb.core.Flyway

trait MigrationConfig extends Config {

  private val flyway = new Flyway()
  flyway.setDataSource(databaseUrl, databaseUser, databasePassword)
  flyway.setBaselineOnMigrate(true)

  def migrate(): Int = {
    flyway.migrate()
  }

  def reloadSchema(): Int = {
    flyway.clean()
    flyway.migrate()
  }

  def dropDatabase(): Unit = flyway.clean()

}
