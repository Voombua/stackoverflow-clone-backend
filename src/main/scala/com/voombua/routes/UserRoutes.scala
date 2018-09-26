package com.voombua.routes

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import com.voombua.core.AuthService
import com.voombua.mapping.JsonProtocol
import com.voombua.messages.{ LoginRequestMessage, UsernameEmailPassword }
import com.voombua.models.UserComponent

import scala.concurrent.duration._
import scala.language.postfixOps

class UserRoutes(repo: UserComponent#UserRepository, auth: AuthService) extends JsonProtocol {
  import scala.concurrent.ExecutionContext.Implicits.global

  protected val createUser: Route = {
    pathPrefix("user") {
      post {
        pathEndOrSingleSlash {
          withRequestTimeout(5 minutes) {

            entity(as[UsernameEmailPassword]) { request ⇒
              complete(StatusCodes.Created → auth.signUp(request.username, request.email, request.password))
            }
          }
        }
      }
    }
  }

  protected val getUsers: Route = {
    pathPrefix("users") {
      get {
        pathEndOrSingleSlash {
          complete(repo.all)
        }
      }
    }
  }

  protected val logIn: Route = {
    pathPrefix("login") {
      post {
        pathEndOrSingleSlash {
          entity(as[LoginRequestMessage]) { request ⇒
            complete(
              auth.signIn(request.login, request.password).map {
                case Some(_) ⇒ StatusCodes.OK
                case None ⇒ StatusCodes.BadRequest
              }
            )
          }
        }
      }
    }
  }

  val routes: Route =
    createUser ~
      getUsers ~
      logIn

}
