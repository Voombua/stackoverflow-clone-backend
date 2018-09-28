package com.voombua.routes

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.voombua.core.{ AnswerService, QuestionService }
import com.voombua.mapping.JsonProtocol
import com.voombua.messages.{ AnswerMessage, AnswerUpdateMessage, QuestionMessage, QuestionUpdateMessage }
import com.voombua.models.{ AnswerComponent, QuestionComponent }
import com.voombua.utils.SecurityDirectives
import spray.json._

import scala.concurrent.ExecutionContext

class QuestionRoute(secretKey: String, repo: QuestionComponent#QuestionRepository, questionService: QuestionService,
  answerService: AnswerService, answerRepo: AnswerComponent#AnswersRepository)(implicit ec: ExecutionContext)
    extends JsonProtocol {
  import SecurityDirectives._
  import StatusCodes._

  private val service = "questions"
  private val answerServiceName = "answers"

  protected val getQuestions: Route = {
    pathPrefix(service) {
      get {
        pathEndOrSingleSlash {
          complete((OK, repo.findUserQuestions().map(_.toJson)))
        }
      }
    }
  }

  protected val postQuestion: Route = {
    pathPrefix(service / "ask") {
      post {
        pathEndOrSingleSlash {
          authenticate(secretKey) { userId ⇒
            entity(as[QuestionMessage]) { request ⇒
              complete((Created, questionService.createQuestion(request, userId).map(_.toJson)))
            }
          }
        }
      }
    }
  }

  protected val deleteQuestion: Route = {
    pathPrefix(service / Segment) { questionId ⇒
      delete {
        pathEndOrSingleSlash {
          authenticate(secretKey) { userId ⇒
            complete((OK, repo.deleteQuestion(questionId, userId).map(_.toJson)))
          }
        }

      }
    }
  }

  protected val updateQuestion: Route = {
    pathPrefix(service / Segment) { questionId ⇒
      put {
        pathEndOrSingleSlash {
          authenticate(secretKey) { _ ⇒
            entity(as[QuestionUpdateMessage]) { request ⇒
              complete(questionService.updateQuestion(questionId, request).map {
                case Some(question) ⇒ OK -> question.toJson
                case None ⇒ BadRequest -> s"Error Updating Question".toJson
              })
            }
          }
        }
      }
    }
  }

  protected val getQuestion: Route = {
    pathPrefix(service / Segment) { questionId ⇒
      pathEndOrSingleSlash {
        get {
          complete(repo.findUserQuestion(questionId).map(_.toJson))
        }
      }
    }
  }

  //TODO(ian): move these answer endpoints to `AnswerRoute` file
  protected val AnswerQuestion: Route = {
    pathPrefix(service / Segment / answerServiceName) { questionId ⇒
      pathEndOrSingleSlash {
        get {
          complete(repo.findUserQuestion(questionId))
        } ~
          post {
            authenticate(secretKey) { userId =>
              entity(as[AnswerMessage]) { answer =>
                complete(answerService.postAnswer(answer, questionId, userId).map(_.toJson))
              }
            }
          }
      }
    }
  }

  protected val updateAnswer: Route = {
    pathPrefix(service / Segment / answerServiceName / Segment) { (questionId, answerId) ⇒
      pathEndOrSingleSlash {
        get {
          complete(repo.findUserQuestion(questionId))
          complete(answerRepo.findAnswer(answerId))
        } ~
          put {
            authenticate(secretKey) { _ ⇒
              entity(as[AnswerUpdateMessage]) { updatedAnswer ⇒
                complete(answerService.updateAnswer(answerId, updatedAnswer).map(_.toJson))
              }
            }
          }
      }
    }
  }

  protected val deleteAnswer: Route = {
    pathPrefix(service / Segment / answerServiceName / Segment) { (questionId, answerId) ⇒
      pathEndOrSingleSlash {
        get {
          complete(repo.findUserQuestion(questionId))
          complete(answerRepo.findAnswer(answerId))
        } ~
          delete {
            authenticate(secretKey) { userId ⇒
              complete((OK, answerRepo.deleteAnswer(answerId, questionId, userId).map(_.toJson)))
            }
          }
      }
    }
  }

  val routes: Route =
    getQuestions ~
      postQuestion ~
      deleteQuestion ~
      updateQuestion ~
      getQuestion ~
      AnswerQuestion ~
      updateAnswer ~
      deleteAnswer

}
