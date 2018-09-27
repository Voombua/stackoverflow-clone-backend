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

case class Question(
  id: QuestionId,
  userId: UserId,
  title: String,
  content: String,
  tags: Option[String],
  created: Timestamp,
  updated: Option[Timestamp],
  deleted: Option[Timestamp]
) extends Entity

case class Answer(
  id: AnswerId,
  userId: UserId,
  questionId: QuestionId,
  content: String,
  created: Timestamp,
  updated: Option[Timestamp],
  deleted: Option[Timestamp]
) extends Entity