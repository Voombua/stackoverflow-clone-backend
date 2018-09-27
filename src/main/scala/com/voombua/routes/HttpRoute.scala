package com.voombua.routes

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.voombua.core.{ AnswerService, AuthService, QuestionService }
import com.voombua.models.{ AnswerComponent, QuestionComponent, UserComponent }
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._

import scala.concurrent.ExecutionContext

class HttpRoute(authService: AuthService, questionService: QuestionService,
    secretKey: String, userRepo: UserComponent#UserRepository,
    questionRepo: QuestionComponent#QuestionRepository, ansService: AnswerService,
    answerRepo: AnswerComponent#AnswersRepository)(implicit ec: ExecutionContext) {
  private val userRoutes: UserRoutes = new UserRoutes(userRepo, authService)
  private val questionRoute: QuestionRoute = new QuestionRoute(secretKey, questionRepo, questionService, ansService)

  val route: Route =
    cors() {
      pathPrefix("v1") {
        userRoutes.routes ~
          questionRoute.routes
      } ~
        pathPrefix("healthchecks") {
          get {
            complete("OK")
          }
        }
    }

}
