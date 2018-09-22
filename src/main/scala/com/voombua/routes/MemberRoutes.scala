package com.voombua.routes

import com.voombua.commands.MemberCommands
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import com.voombua.messages.{ CreateMemberRequest, MemberMarshaller }

import scala.concurrent.duration._

class MemberRoutes(commands: MemberCommands) extends MemberMarshaller {
  val service = "users"
  val version = "v1"
  protected val createMember: Route = {
    pathPrefix(service / version / "member") {
      post {
        pathEndOrSingleSlash {
          withRequestTimeout(5 minutes) {
            entity(as[CreateMemberRequest]) { request ⇒
              complete((StatusCodes.Created, commands.createMember(request)))
            }
          }
        }
      }
    }
  }

  val routes: Route =
    createMember

}
