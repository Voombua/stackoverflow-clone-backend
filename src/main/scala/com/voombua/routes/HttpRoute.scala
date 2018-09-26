package com.voombua.routes

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.voombua.core.{ AuthService, QuestionService }
import com.voombua.models.{ QuestionComponent, UserComponent }
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._

import scala.concurrent.ExecutionContext

class HttpRoute(authService: AuthService, questionService: QuestionService,
    secretKey: String, userRepo: UserComponent#UserRepository,
    questionRepo: QuestionComponent#QuestionRepository)(implicit ec: ExecutionContext) {
  private val userRoutes: UserRoutes = new UserRoutes(userRepo, authService)
  private val questionRoute: QuestionRoute = new QuestionRoute(secretKey, questionRepo, questionService)

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
