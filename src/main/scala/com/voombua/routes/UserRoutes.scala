package com.voombua.routes

import java.sql.Timestamp
import java.time.Instant

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import com.voombua.mapping.JsonProtocol
import com.voombua.models.{ User, UserComponent }
import java.util.UUID

import scala.concurrent.duration._
import scala.language.postfixOps

class UserRoutes(repo: UserComponent#UserRepository) extends JsonProtocol {
  val service = "users"
  val version = "v1"
  protected val createUser: Route = {
    pathPrefix(service / version / "user") {
      post {
        pathEndOrSingleSlash {
          withRequestTimeout(5 minutes) {

            entity(as[User]) { request ⇒
              val r = request.copy(id = Some(UUID.randomUUID()), created = Some(Timestamp.from(Instant.now())))
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

  val routes: Route =
    createUser ~
      getUsers

}
