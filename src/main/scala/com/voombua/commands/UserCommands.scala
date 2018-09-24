package com.voombua.commands

import com.voombua.messages.LoginRequestMessage
import com.voombua.repos.UserDao

import scala.concurrent.ExecutionContext

object UserCommands {

  def loginByEmail(request: LoginRequestMessage)(implicit ec: ExecutionContext) = {
    for {
      e ← UserDao.findByEmail(request.email)
      _ ← UserDao.findById(e.id.get)
      res = if (e.email == request.email && e.password == request.password) true else false
    } yield res
  }

}
