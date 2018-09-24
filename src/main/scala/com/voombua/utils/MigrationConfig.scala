package com.voombua.utils

import org.flywaydb.core.Flyway

trait MigrationConfig extends Config {

  private val flyway = new Flyway()
  flyway.setDataSource(databaseUrl, databaseUser, databasePassword)
  flyway.setBaselineOnMigrate(true)

  def migrate() = {
    flyway.migrate()
  }

  def reloadSchema() = {
    flyway.clean()
    flyway.migrate()
  }

}
