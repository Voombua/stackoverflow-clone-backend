package com.voombua.core

import java.sql.Timestamp
import java.time.Instant
import java.util.UUID

import com.voombua.messages.{ AnswerMessage, AnswerUpdateMessage }
import com.voombua.models.{ Answer, AnswerComponent }
import com.voombua.utils.MonadTransformers._

import scala.concurrent.{ ExecutionContext, Future }

class AnswerService(answerStorage: AnswerComponent#AnswersRepository)(implicit ec: ExecutionContext) {
  def postAnswer(request: AnswerMessage, questionId: QuestionId, userId: UserId): Future[Answer] = {
    answerStorage.saveAnswer(Answer(UUID.randomUUID().toString, questionId, userId, request.content,
      Timestamp.from(Instant.now()), None, None))
  }

  def updateAnswer(answerId: AnswerId, request: AnswerUpdateMessage): Future[Option[Answer]] = {
    answerStorage
      .findAnswer(answerId)
      .mapT(request.merge)
      .flatMapTOuter(answerStorage.saveAnswer)
  }
}
