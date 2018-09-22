package com.voombua.repos

import com.datastax.driver.core.{ PlainTextAuthProvider, SocketOptions }
import com.outworkers.phantom.dsl._
import com.voombua.models.MemberEntities

import scala.language.reflectiveCalls

class ServiceDb(connector: CassandraConnection) extends Database[ServiceDb](connector) {
  object Members extends MemberEntities with Connector {
    override lazy val tableName = "members"
  }
}

object DbConnector {

  val default: CassandraConnection = ContactPoint.local
    .withClusterBuilder(
      _.withSocketOptions(
        new SocketOptions()
          .setConnectTimeoutMillis(20000)
          .setReadTimeoutMillis(20000)
      ).withAuthProvider(
          new PlainTextAuthProvider("username", "password")
        )
    ).keySpace(
        KeySpace("stackoverflowclone").ifNotExists().`with`(
          replication eqs SimpleStrategy.replication_factor(1)
        )
      )
}
