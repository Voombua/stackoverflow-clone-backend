package com.voombua.routes

import java.sql.Timestamp
import java.time.Instant
import java.util.UUID

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import com.roundeights.hasher.Implicits._
import com.voombua.core.JWT
import com.voombua.mapping.JsonProtocol
import com.voombua.messages.LoginRequestMessage
import com.voombua.models.{User, UserComponent}

import scala.concurrent.duration._
import scala.language.postfixOps

class UserRoutes(repo: UserComponent#UserRepository) extends JsonProtocol with JWT {
  import scala.concurrent.ExecutionContext.Implicits.global
  val service = "members"
  val version = "v1"
  protected val createUser: Route = {
    pathPrefix(service / version / "user") {
      post {
        pathEndOrSingleSlash {
          withRequestTimeout(5 minutes) {

            entity(as[User]) { request ⇒
              val r = request.copy(id = Some(UUID.randomUUID()), password = request.password.sha256.hex,
                created = Some(Timestamp.from(Instant.now())))
              complete((StatusCodes.Created, repo.insert(r)))
            }
          }
        }
      }
    }
  }

  protected val getUsers: Route = {
    pathPrefix(service / version / "users") {
      get {
        pathEndOrSingleSlash {
          complete(repo.all)
        }
      }
    }
  }

  protected val logIn: Route = {
    pathPrefix(service / version / "login") {
      pathEndOrSingleSlash {
        entity(as[LoginRequestMessage]) { request ⇒
          complete(
            repo.login(request.login, request.password).map {
              case Some(_) ⇒ StatusCodes.OK
              case None ⇒ StatusCodes.BadRequest
            }
          )
        }
      }
    }
  }

  val routes: Route =
    createUser ~
      getUsers ~
      logIn

}
