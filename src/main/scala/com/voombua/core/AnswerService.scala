package com.voombua.core

import java.sql.Timestamp
import java.time.Instant
import java.util.UUID

import com.voombua.messages.AnswerMessage
import com.voombua.models.{ Answer, AnswerComponent }

import scala.concurrent.{ ExecutionContext, Future }

class AnswerService(answerStorage: AnswerComponent#AnswersRepository)(implicit ec: ExecutionContext) {
  def postAnswer(request: AnswerMessage, questionId: QuestionId, userId: UserId): Future[Answer] = {
    answerStorage.saveAnswer(Answer(UUID.randomUUID().toString, questionId, userId, request.content,
      Timestamp.from(Instant.now()), None, None))
  }
}
