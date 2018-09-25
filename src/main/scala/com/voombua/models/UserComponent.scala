package com.voombua.models

import com.voombua.repos.{ DB, RepoDefinition }

import scala.concurrent.{ ExecutionContext, Future }
import com.roundeights.hasher.Implicits._
trait UserComponent extends RepoDefinition { this: DB ⇒
  import driver.api._
  class UsersTable(tag: Tag) extends BaseTable[User](tag, "users") {
    val username = column[String]("username")
    val email = column[String]("email")
    val password = column[String]("password")
    def * = (id.?, username, email, password, created.?, updated.?, deleted.?) <> ((User.apply _).tupled, User.unapply)
  }

  class UserRepository(implicit ex: ExecutionContext) extends BaseRepo[User, UsersTable] with Authentication {
    override val table = TableQuery[UsersTable]

    override def login(login: String, password: String): Future[Option[User]] =
      db.run(table.filter(d => d.username === login || d.email === login && d.password === password.sha256.hex).result.headOption)

  }

}

sealed trait Authentication {
  def login(login: String, password: String): Future[Option[User]]
}

//class Auth(ex: ExecutionContext) extends Authentication {
//  override def login(login: String): Future[Option[User]] =
//
//}

