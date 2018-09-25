package com.voombua.utils

object DatabaseConfig {
  val driver = slick.jdbc.PostgresProfile

  import driver.api._

  def db = Database.forConfig("database")

  implicit val session: Session = db.createSession()
  implicit val closeSession: Unit = db.close()

}
