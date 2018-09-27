package com.voombua.routes

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.voombua.core.{ AnswerService, QuestionService }
import com.voombua.mapping.JsonProtocol
import com.voombua.messages.{ AnswerMessage, QuestionMessage, QuestionUpdateMessage }
import com.voombua.models.QuestionComponent
import com.voombua.utils.SecurityDirectives
import spray.json._

import scala.concurrent.ExecutionContext

class QuestionRoute(secretKey: String, repo: QuestionComponent#QuestionRepository, questionService: QuestionService,
    answerService: AnswerService)(implicit ec: ExecutionContext) extends JsonProtocol {
  import SecurityDirectives._
  import StatusCodes._

  protected val getQuestions: Route = {
    pathPrefix("questions") {
      get {
        pathEndOrSingleSlash {
          complete((OK, repo.findUserQuestions().map(_.toJson)))
        }
      }
    }
  }

  protected val postQuestion: Route = {
    pathPrefix("ask") {
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
    pathPrefix("delete-question" / Segment) { questionId ⇒
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
    pathPrefix("edit-question" / Segment) { questionId ⇒
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

  protected val getOrAnswerQuestion: Route = {
    pathPrefix("question" / Segment) { questionId ⇒
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

  val routes: Route =
    getQuestions ~
      postQuestion ~
      deleteQuestion ~
      updateQuestion ~
      getOrAnswerQuestion

}
