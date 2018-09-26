package com.voombua

package object core {
  type UserId = String
  type AuthToken = String

  final case class AuthTokenContent(userId: UserId)

  final case class UserProfile(id: UserId, firstName: String, lastName: String)

  final case class UserProfileUpdate(firstName: Option[String] = None, lastName: Option[String] = None)

}
