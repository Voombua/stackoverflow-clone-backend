package com.voombua.models

import com.voombua.core.{ QuestionId, UserId }
import com.voombua.repos.{ DB, RepoDefinition }

import scala.concurrent.{ ExecutionContext, Future }

trait QuestionComponent extends RepoDefinition with UserComponent { this: DB ⇒
  import driver.api._
  class QuestionsTable(tag: Tag) extends BaseTable[Question](tag, "questions") {
    val userId = column[String]("user_id")
    val title = column[String]("title")
    val content = column[String]("content")
    val tags = column[String]("tags")

    def * = (id, userId, title, content, tags.?, created, updated.?, deleted.?) <>
      ((Question.apply _).tupled, Question.unapply)

    def author = foreignKey("user_fk", userId, TableQuery[UsersTable])(_.id)

  }

  class QuestionRepository(implicit ec: ExecutionContext) extends BaseRepo[Question, QuestionsTable] with QuestionsDAO {
    override val table = TableQuery[QuestionsTable]

    override def findUserQuestions(): Future[Seq[Question]] = {
      db.run(table.result)
    }

    override def findByUserIdAndQuestionId(userId: UserId, questionId: QuestionId): Future[Question] = {
      db.run(table.filter(res ⇒ res.userId === userId || res.userId === questionId).result.head)
    }

    override def updateQuestion(newQuestion: Question, questionId: QuestionId): Future[Int] = {
      db.run(table.filter(_.id === questionId).map(question ⇒ (question.title, question.content))
        .update((newQuestion.title, newQuestion.content)))

    }

    override def deleteQuestion(questionId: QuestionId): Future[Int] =
      db.run(table.filter(_.id === questionId).delete)

    override def saveQuestion(question: Question): Future[Question] =
      db.run(table.insertOrUpdate(question)).map(_ => question)
  }
}

sealed trait QuestionsDAO {
  def findUserQuestions(): Future[Seq[Question]]
  def findByUserIdAndQuestionId(userId: UserId, questionId: QuestionId): Future[Question]
  def updateQuestion(newQuestion: Question, questionId: QuestionId): Future[Int]
  def deleteQuestion(questionId: QuestionId): Future[Int]
  def saveQuestion(question: Question): Future[Question]
}
