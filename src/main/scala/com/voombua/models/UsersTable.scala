package com.voombua.models

import slick.jdbc.PostgresProfile.api._

class UsersTable(tag: Tag) extends Table[User](tag, "users") {
  def id = column[UserId]("id", O.PrimaryKey, O.AutoInc)
  def username = column[String]("username")
  def email = column[String]("email")
  def password = column[String]("password")
  def * = (id.?, username, password, email) <> ((User.apply _).tupled, User.unapply)
}
