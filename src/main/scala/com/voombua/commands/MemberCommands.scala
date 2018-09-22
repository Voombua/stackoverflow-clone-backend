package com.voombua.commands

import java.util.UUID

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.voombua.messages.CreateMemberRequest
import com.voombua.models.MemberEntity
import com.voombua.repos.ServiceDb

import scala.concurrent.Future
import scala.util.control.NonFatal

class MemberCommands(db: ServiceDb)(implicit system: ActorSystem, mat: ActorMaterializer) {
  import scala.concurrent.ExecutionContext.Implicits.global

  type Recovery[T] = PartialFunction[Throwable, T]

  //  recover with None
  def withNone[T]: Recovery[Option[T]] = { case NonFatal(_) ⇒ None }

  //  recovery with empty sequence
  def withEmptySeq[T]: Recovery[Seq[T]] = { case NonFatal(_) ⇒ Seq() }

  def exists(id: UUID, email: String, username: String): Future[Boolean] = {
    for {
      i ← db.Members.getUserById(id)
      u ← db.Members.getUserByUsername(id)
    } yield i.isDefined || u.isDefined
  }

  def createMember(request: CreateMemberRequest): Future[MemberEntity] = {
    val memberId = UUID.randomUUID()
    for {
      e ← exists(memberId, request.email, request.username)
      _ = if (e) s"Someone with this ${request.username} username exists" else Unit
      member = MemberEntity.create(memberId, request.username, request.email, request.password)
    } yield member
  }
}

