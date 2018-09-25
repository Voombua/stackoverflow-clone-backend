package com.voombua.models

import com.voombua.repos.{ DB, RepoDefinition }

import scala.concurrent.ExecutionContext

trait UserComponent extends RepoDefinition { this: DB ⇒
  import driver.api._
  class UsersTable(tag: Tag) extends BaseTable[User](tag, "users") {
    val username = column[String]("username")
    val email = column[String]("email")
    val password = column[String]("password")
    def * = (id.?, username, email, password, created.?, updated.?, deleted.?) <> ((User.apply _).tupled, User.unapply)
  }

  class UserRepository(implicit ex: ExecutionContext) extends BaseRepo[User, UsersTable] {
    override val table = TableQuery[UsersTable]

  }

}

