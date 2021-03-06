package com.voombua.mapping

import java.sql.Timestamp
import java.time.Instant
import java.util.UUID

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.voombua.messages.{ AnswerUpdateMessage, _ }
import com.voombua.models.{ Answer, Question, User }
import spray.json.{ DefaultJsonProtocol, JsNumber, JsString, JsValue, JsonFormat, RootJsonFormat }

trait BaseJsonProtocol extends DefaultJsonProtocol {
  implicit val timestampFormat: JsonFormat[Timestamp] = new JsonFormat[Timestamp] {
    override def write(obj: Timestamp): JsValue = JsNumber(obj.getTime)

    override def read(json: JsValue): Timestamp = json match {
      case JsNumber(x) => Timestamp.from(Instant.ofEpochMilli(x.toLong))
      case _ =>
        throw new IllegalArgumentException(
          s"Can not parse json value [$json] to a timestamp object"
        )
    }
  }

  implicit val uuidJsonFormat: JsonFormat[UUID] = new JsonFormat[UUID] {
    override def write(x: UUID): JsValue = JsString(x.toString)

    override def read(value: JsValue): UUID = value match {
      case JsString(x) => UUID.fromString(x)
      case x =>
        throw new IllegalArgumentException("Expected UUID as JsString, but got " + x.getClass)
    }
  }
}

/**
 * Implicit json conversion -> Nothing to do when we complete the object
 */
trait JsonProtocol extends SprayJsonSupport with BaseJsonProtocol {
  implicit val userFormat: RootJsonFormat[User] = jsonFormat7(User)
  implicit val loginFormat: RootJsonFormat[LoginRequestMessage] = jsonFormat2(LoginRequestMessage)
  implicit val userNameEmailPasswordFormat: RootJsonFormat[UsernameEmailPassword] = jsonFormat3(UsernameEmailPassword)
  implicit val questionFormat: RootJsonFormat[Question] = jsonFormat8(Question)
  implicit val questionRequestFormat: RootJsonFormat[QuestionMessage] = jsonFormat3(QuestionMessage)
  implicit val questionUpdateMessageFormat: RootJsonFormat[QuestionUpdateMessage] = jsonFormat3(QuestionUpdateMessage)
  implicit val answerFormat: RootJsonFormat[Answer] = jsonFormat7(Answer)
  implicit val answerMessageFormat: RootJsonFormat[AnswerMessage] = jsonFormat1(AnswerMessage)
  implicit val answerUpdateMessage: RootJsonFormat[AnswerUpdateMessage] = jsonFormat1(AnswerUpdateMessage)
}
