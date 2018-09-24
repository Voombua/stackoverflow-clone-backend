package com.voombua.routes

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import com.voombua.commands.UserCommands
import com.voombua.mapping.JsonMapping
import com.voombua.messages.LoginRequestMessage
import com.voombua.models.User
import com.voombua.repos.UserDao

import scala.concurrent.duration._
import scala.util.{ Failure, Success }

class UserRoutes extends JsonMapping {
  val service = "users"
  val version = "v1"
  protected val createMember: Route = {
    pathPrefix(service / version / "member") {
      post {
        pathEndOrSingleSlash {
          withRequestTimeout(5 minutes) {

            entity(as[User]) { request ⇒
              complete((StatusCodes.Created, UserDao.create(request)))
            }
          }
        }
      }
    }
  }

  protected val loginByEmail: Route = {
    import scala.concurrent.ExecutionContext.Implicits.global
    pathPrefix(service / version / "login") {
      post {
        pathEndOrSingleSlash {
          entity(as[LoginRequestMessage]) { loginData ⇒
            onComplete(UserCommands.loginByEmail(loginData)) {
              case Success(user) ⇒ if (user) {
                complete(StatusCodes.OK)
              } else {
                complete(StatusCodes.Forbidden)
              }
              case Failure(_) ⇒ complete(StatusCodes.NotFound)
            }

          }
        }
      }
    }
  }

  val routes: Route =
    createMember ~
      loginByEmail

}
