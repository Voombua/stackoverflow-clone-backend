package com.voombua.mapping

import com.voombua.messages.LoginRequestMessage
import com.voombua.models.User
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport
import play.api.libs.json._

trait JsonMapping extends PlayJsonSupport {
  implicit val userFormat: OFormat[User] = Json.format[User]
  implicit val loginRequestFormat: OFormat[LoginRequestMessage] = Json.format[LoginRequestMessage]

}
