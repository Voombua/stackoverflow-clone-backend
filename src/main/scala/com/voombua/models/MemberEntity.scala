package com.voombua.models

import com.outworkers.phantom.dsl._
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport
import play.api.libs.json._

import scala.concurrent.Future

case class MemberEntity(
  memberId: UUID,
  username: String,
  email: String,
  password: String
)

object MemberEntity extends PlayJsonSupport {
  implicit val memberFormat: OFormat[MemberEntity] = Json.format[MemberEntity]

  def create(
    memberId: UUID,
    username: String,
    email: String,
    password: String
  ): MemberEntity = {
    MemberEntity(memberId, username, email.toLowerCase.trim, password)
  }
}

abstract class MemberEntities extends Table[MemberEntities, MemberEntity] {

  object memberId extends UUIDColumn with PartitionKey {
    override lazy val name = "member_id"
  }
  object username extends StringColumn
  object email extends StringColumn
  object password extends StringColumn

  def store(model: MemberEntity): Future[ResultSet] = {
    insert
      .value(_.memberId, model.memberId)
      .value(_.username, model.username)
      .value(_.email, model.username)
      .value(_.password, model.password)
      .future()
  }

  def getUserById(id: UUID): Future[Option[MemberEntity]] = {
    select.where(_.memberId eqs id).one()
  }

  def getUserByUsername(id: UUID): Future[Option[String]] = {
    select(_.username).where(_.memberId eqs id).one()
  }

  def updateUsername(id: UUID, newUsername: String): Future[ResultSet] = {
    update.where(_.memberId eqs id)
      .modify(_.username setTo newUsername)
      .future()
  }

}
