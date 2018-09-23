package com.voombua.routes

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import com.voombua.mapping.JsonMapping
import com.voombua.models.User
import com.voombua.repos.UserDao
import scala.concurrent.duration._

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

  val routes: Route =
    createMember

}
