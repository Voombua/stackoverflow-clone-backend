package com.voombua.messages

import play.api.libs.json._
import de.heikoseeberger.akkahttpplayjson._

case class CreateMemberRequest(username: String, password: String, email: String)

trait MemberMarshaller extends PlayJsonSupport {
  implicit val memberFormat: OFormat[CreateMemberRequest] = Json.format[CreateMemberRequest]
}

