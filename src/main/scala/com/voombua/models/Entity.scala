package com.voombua.models

import java.sql.Timestamp
import java.util.UUID

trait Entity {
  def id: Option[UUID]
  def created: Option[Timestamp]
  def updated: Option[Timestamp]
  def deleted: Option[Timestamp]
}

case class User(
  id: Option[UUID],
  username: String,
  email: String,
  password: String,
  created: Option[Timestamp],
  updated: Option[Timestamp],
  deleted: Option[Timestamp]
) extends Entity

case class Post(
  id: Option[UUID],
  userId: UUID,
  title: String,
  content: String,
  tags: String,
  created: Option[Timestamp],
  updated: Option[Timestamp],
  deleted: Option[Timestamp]
) extends Entity

case class Comment(
  id: Option[UUID],
  userId: UUID,
  postId: UUID,
  content: String,
  created: Option[Timestamp],
  updated: Option[Timestamp],
  deleted: Option[Timestamp]
) extends Entity