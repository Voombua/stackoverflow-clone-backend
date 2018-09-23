package com.voombua.utils

trait DatabaseConfig {
  val driver = slick.jdbc.PostgresProfile
  //  val driver2 = slick.jdbc.PostgresProfile

  import driver.api._

  def db = Database.forConfig("database")

  implicit val session: Session = db.createSession()

}
