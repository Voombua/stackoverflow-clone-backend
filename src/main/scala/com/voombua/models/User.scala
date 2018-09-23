package com.voombua.models

case class User(id: Option[UserId], userName: String, email: String, password: String)
