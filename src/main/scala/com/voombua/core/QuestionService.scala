package com.voombua.core

import java.sql.Timestamp
import java.time.Instant
import java.util.UUID

import com.voombua.messages.QuestionMessage
import com.voombua.models.{ Question, QuestionComponent }

import scala.concurrent.{ ExecutionContext, Future }

class QuestionService(questionStorage: QuestionComponent#QuestionRepository)(implicit ec: ExecutionContext) {

  def createQuestion(request: QuestionMessage, userId: UserId): Future[Question] = {
    questionStorage.saveQuestion(Question(UUID.randomUUID().toString, userId, request.title,
      request.content, request.tags, Timestamp.from(Instant.now()), None, None))
  }

}
