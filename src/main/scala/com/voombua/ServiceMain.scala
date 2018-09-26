package com.voombua

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.stream.ActorMaterializer
import com.voombua.core.AuthService
import com.voombua.models.UserComponent
import com.voombua.repos.{ DB, PG }
import com.voombua.routes.UserRoutes
import com.voombua.utils.{ Config, MigrationConfig }

import scala.concurrent.{ ExecutionContextExecutor, Future }

object ServiceMain extends App with Config with MigrationConfig with UserComponent with DB with PG {

  implicit val system: ActorSystem = ActorSystem() // ActorMaterializer requires an implicit ActorSystem
  implicit val ec: ExecutionContextExecutor = system.dispatcher // bindingFuture.map requires an implicit ExecutionContext
  val userRepo: UserRepository = new UserRepository()
  val auth = new AuthService(userRepo, "my_secret_key")

  val api = new UserRoutes(userRepo, auth).routes // the RestApi provides a Route

  implicit val materializer: ActorMaterializer = ActorMaterializer() // bindAndHandle requires an implicit materializer

  migrate()

  val bindingFuture: Future[ServerBinding] = Http().bindAndHandle(api, httpInterface, httpPort) // starts the HTTP server

  val log = Logging(system.eventStream, "stack-overflow-clone")

  try {
    //    Here we start the HTTP server and log the info
    bindingFuture.map { serverBinding ⇒
      log.info(s"RestApi bound to ${serverBinding.localAddress}")
    }
  } catch {
    //    If the HTTP server fails to start, we throw an Exception and log the error and close the system
    case ex: Exception ⇒
      log.error(ex, "Failed to bind to {}:{}!", httpInterface, httpPort)
      //      System shutdown
      system.terminate()
  }
}

