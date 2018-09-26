package com.voombua.models

import java.sql.Timestamp

import com.voombua.core._

trait Entity {
  def id: String
  def created: Timestamp
  def updated: Option[Timestamp]
  def deleted: Option[Timestamp]
}

case class User(
  id: UserId,
  username: String,
  email: String,
  password: String,
  created: Timestamp,
  updated: Option[Timestamp],
  deleted: Option[Timestamp]
) extends Entity

case class Post(
  id: String,
  userId: UserId,
  title: String,
  content: String,
  tags: String,
  created: Timestamp,
  updated: Option[Timestamp],
  deleted: Option[Timestamp]
) extends Entity

case class Comment(
  id: String,
  userId: UserId,
  postId: String,
  content: String,
  created: Timestamp,
  updated: Option[Timestamp],
  deleted: Option[Timestamp]
) extends Entity