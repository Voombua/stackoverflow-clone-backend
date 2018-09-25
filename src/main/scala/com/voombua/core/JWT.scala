package com.voombua.core

import java.util.concurrent.TimeUnit

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import authentikat.jwt.{ JsonWebToken, JwtClaimsSet, JwtHeader }

trait JWT {
  val tokenExpiryPeriodInDays = 1
  val secretKey = "super_secret_key"
  val header = JwtHeader("HS256")

  def securedContent: Route = get {
    authenticated { claims =>
      complete(s"User ${claims.getOrElse("user", "")} accessed secured content!")
    }
  }

  def authenticated: Directive1[Map[String, Any]] =
    optionalHeaderValueByName("Authorization").flatMap {
      case Some(jwt) if isTokenExpired(jwt) =>
        complete(StatusCodes.Unauthorized -> "Token expired.")

      case Some(jwt) if JsonWebToken.validate(jwt, secretKey) =>
        provide(getClaims(jwt).getOrElse(Map.empty[String, Any]))

      case _ => complete(StatusCodes.Unauthorized)
    }

  def setClaims(username: String, expiryPeriodInDays: Long) = JwtClaimsSet(
    Map(
      "user" -> username,
      "expiredAt" -> (System.currentTimeMillis() + TimeUnit.DAYS
        .toMillis(expiryPeriodInDays))
    )
  )

  def getClaims(jwt: String): Option[Map[String, String]] = jwt match {
    case JsonWebToken(_, claims, _) => claims.asSimpleMap.toOption
    case _ => None
  }

  def isTokenExpired(jwt: String): Boolean = getClaims(jwt) match {
    case Some(claims) =>
      claims.get("expiredAt") match {
        case Some(value) => value.toLong < System.currentTimeMillis()
        case None => false
      }
    case None => false
  }

}
