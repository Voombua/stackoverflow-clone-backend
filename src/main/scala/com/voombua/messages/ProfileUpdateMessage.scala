package com.voombua.messages

import com.voombua.models.{ Comment, Post }

case class ProfileUpdateMessage(
  username: Option[String],
    image: Option[String] = None,
    posts: Option[Seq[Post]] = None,
    comments: Option[Seq[Comment]] = None
) {

}
