package com.voombua.messages

case class QuestionMessage(title: String, content: String, tags: Option[String] = None)
