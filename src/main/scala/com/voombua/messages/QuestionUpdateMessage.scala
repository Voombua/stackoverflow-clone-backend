package com.voombua.messages

import java.sql.Timestamp
import java.time.Instant

import com.voombua.models.Question

case class QuestionUpdateMessage(title: Option[String] = None, content: Option[String] = None,
    tags: Option[String] = None) {
  def merge(question: Question): Question =
    Question(question.id, question.userId, title.getOrElse(question.title), content.getOrElse(question.content),
      tags, question.created, Some(Timestamp.from(Instant.now())), None)
}
