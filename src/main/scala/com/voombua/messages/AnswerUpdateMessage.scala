package com.voombua.messages

import java.sql.Timestamp
import java.time.Instant

import com.voombua.models.Answer

case class AnswerUpdateMessage(content: Option[String]) {
  def merge(answer: Answer): Answer = {
    Answer(answer.id, answer.questionId, answer.userId, content.getOrElse(answer.content), answer.created,
      Some(Timestamp.from(Instant.now())), None)
  }

}
