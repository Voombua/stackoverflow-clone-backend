package com.voombua.utils

import akka.util.Timeout

trait RequestTimeout extends Config {
  import scala.concurrent.duration._
  def requestTimeout(config: Config): Timeout = {
    val t = time
    val d = Duration(t)
    FiniteDuration(d.length, d.unit)
  }
}
