package com.voombua.routes

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.voombua.core.QuestionService
import com.voombua.mapping.JsonProtocol
import com.voombua.messages.QuestionMessage
import com.voombua.models.QuestionComponent
import com.voombua.utils.SecurityDirectives

import scala.concurrent.ExecutionContext
import spray.json._

class QuestionRoute(secretKey: String, repo: QuestionComponent#QuestionRepository, qService: QuestionService)(implicit ec: ExecutionContext) extends JsonProtocol {
  import SecurityDirectives._
  import StatusCodes._

  //  val service = "questions"
  //  val version = "v1"

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
      pathEndOrSingleSlash {
        authenticate(secretKey) { userId ⇒
          entity(as[QuestionMessage]) { request ⇒
            complete((Created, qService.createQuestion(request, userId).map(_.toJson)))
          }
        }
      }
    }
  }

  val routes: Route =
    getQuestions ~
      postQuestion

}
