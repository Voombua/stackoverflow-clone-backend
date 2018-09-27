package com.voombua.models

import com.voombua.core.QuestionId
import com.voombua.repos.{DB, RepoDefinition}

import scala.concurrent.{ExecutionContext, Future}

trait AnswerComponent extends RepoDefinition with QuestionComponent {
  this: DB ⇒

  import driver.api._

  class AnswersTable(tag: Tag) extends BaseTable[Answer](tag, "answers") {
    val questionId = column[String]("question_id")
    val userId = column[String]("user_id")
    val content = column[String]("content")

    def * = (id, questionId, userId, content, created, updated.?, deleted.?) <>
      ((Answer.apply _).tupled, Answer.unapply)

    def author = foreignKey("answers_user_fk", userId, TableQuery[UsersTable])(_.id)
    def question = foreignKey("answers_question_fk", questionId, TableQuery[QuestionsTable])(_.id)

  }

  class AnswersRepository(implicit ec: ExecutionContext) extends BaseRepo[Answer, AnswersTable] with AnswerDAO {
    override val table = TableQuery[AnswersTable]

    override def findAllAnswers(): Future[Seq[Answer]] = db.run(table.result)

    override def findAllAnswersToQuestion(questionId: QuestionId): Future[Seq[Answer]] =
      db.run(table.filter(res ⇒ res.questionId === questionId).result)

    override def saveAnswer(answer: Answer): Future[Answer] =
      db.run(table.insertOrUpdate(answer)).map(_ => answer)

  }

}

sealed trait AnswerDAO {
  def findAllAnswers(): Future[Seq[Answer]]
  def findAllAnswersToQuestion(questionId: QuestionId): Future[Seq[Answer]]
  def saveAnswer(answer: Answer): Future[Answer]
}
