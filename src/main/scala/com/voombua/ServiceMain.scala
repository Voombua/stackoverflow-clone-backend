package com.voombua

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.stream.ActorMaterializer
import com.voombua.core.{ AuthService, QuestionService }
import com.voombua.models.{ QuestionComponent, UserComponent }
import com.voombua.repos.{ DB, PG }
import com.voombua.routes.{ HttpRoute, UserRoutes }
import com.voombua.utils.{ Config, MigrationConfig }

import scala.concurrent.{ ExecutionContextExecutor, Future }

object ServiceMain extends App with Config with MigrationConfig with UserComponent with DB with PG with QuestionComponent {

  implicit val system: ActorSystem = ActorSystem()
  implicit val ec: ExecutionContextExecutor = system.dispatcher
  val userRepo: UserRepository = new UserRepository()
  val questionRepo = new QuestionRepository()
  val auth = new AuthService(userRepo, "my_secret_key")
  val questionService = new QuestionService(questionRepo)
  val httpRoute = new HttpRoute(auth, questionService, "my_secret_key", userRepo, questionRepo).route

  implicit val materializer: ActorMaterializer = ActorMaterializer()

  migrate()

  val bindingFuture: Future[ServerBinding] = Http().bindAndHandle(httpRoute, httpInterface, httpPort)

  val log = Logging(system.eventStream, "stack-overflow-clone")

  try {
    bindingFuture.map { serverBinding ⇒
      log.info(s"RestApi bound to ${serverBinding.localAddress}")
    }
  } catch {
    case ex: Exception ⇒
      log.error(ex, "Failed to bind to {}:{}!", httpInterface, httpPort)
      //      System shutdown
      system.terminate()
  }
}

