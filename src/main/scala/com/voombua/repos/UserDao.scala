package com.voombua.repos

import com.voombua.models.UserId
import com.voombua.models.User
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Future

object UserDao extends BaseDao {
  def findAll: Future[Seq[User]] = usersTable.result
  def findById(userId: UserId): Future[User] = usersTable.filter(_.id === userId).result.head
  def create(user: User): Future[UserId] = usersTable returning usersTable.map(_.id) += user
  def update(newUser: User, userId: UserId): Future[Int] = usersTable.filter(_.id === userId)
    .map(user => (user.username, user.password, user.email))
    .update((newUser.userName, newUser.password, newUser.email))

  def delete(userId: UserId): Future[Int] = usersTable.filter(_.id === userId).delete

  def findByEmail(email: String): Future[User] = usersTable.filter(user ⇒ user.email === email).result.head

}
