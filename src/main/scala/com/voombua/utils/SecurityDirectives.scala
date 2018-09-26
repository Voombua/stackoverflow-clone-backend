package com.voombua.utils

import akka.http.scaladsl.server.Directive1
import akka.http.scaladsl.server.directives.{ BasicDirectives, HeaderDirectives, RouteDirectives }
import com.voombua.core.{ AuthTokenContent, UserId }
import io.circe.parser._
import io.circe.generic.auto._
import pdi.jwt._
object SecurityDirectives {

  import BasicDirectives._
  import HeaderDirectives._
  import RouteDirectives._

  def authenticate(secretKey: String): Directive1[UserId] =
    headerValueByName("Token")
      .map(Jwt.decodeRaw(_, secretKey, Seq(JwtAlgorithm.HS256)))
      .map(_.toOption.flatMap(decode[AuthTokenContent](_).toOption))
      .flatMap {
        case Some(result) =>
          provide(result.userId)
        case None =>
          reject
      }

}
