package com.voombua

import java.util.UUID

package object core {
  type UserId = String
  type AuthToken = String

  final case class AuthTokenContent(userId: UserId)

  final case class UserProfile(id: UserId, firstName: String, lastName: String) /*{
    require(id.toString.nonEmpty, "firstName.empty")
    require(firstName.nonEmpty, "firstName.empty")
    require(lastName.nonEmpty, "lastName.empty")
  }*/

  final case class UserProfileUpdate(firstName: Option[String] = None, lastName: Option[String] = None) /*{
    def merge(profile: UserProfile): UserProfile =
      UserProfile(profile.id, firstName.getOrElse(profile.firstName), lastName.getOrElse(profile.lastName))
  }*/

}
