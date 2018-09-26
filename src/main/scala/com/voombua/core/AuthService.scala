package com.voombua.core

import java.sql.Timestamp
import java.time.Instant
import java.util.UUID

import com.roundeights.hasher.Implicits._
import com.voombua.models.{ User, UserComponent }
import pdi.jwt.{ Jwt, JwtAlgorithm }
import io.circe.syntax._
import io.circe.generic.auto._
import com.voombua.utils.MonadTransformers._

import scala.concurrent.{ ExecutionContext, Future }

class AuthService(
    authDataStorage: UserComponent#UserRepository,
    secretKey: String
)(implicit executionContext: ExecutionContext) {

  def signIn(login: String, password: String): Future[Option[AuthToken]] =
    authDataStorage
      .login(login, password)
      .filterT(_.password == password.sha256.hex)
      .mapT(authData ⇒ encodeToken(authData.id))

  def signUp(login: String, email: String, password: String): Future[AuthToken] =
    authDataStorage
      .UserSignUp(User(UUID.randomUUID().toString, login, email, password.sha256.hex, Timestamp.from(Instant.now()), None, None))
      .map(authData => encodeToken(authData.id))

  private def encodeToken(userId: UserId): AuthToken =
    Jwt.encode(AuthTokenContent(userId).asJson.noSpaces, secretKey, JwtAlgorithm.HS256)

}
