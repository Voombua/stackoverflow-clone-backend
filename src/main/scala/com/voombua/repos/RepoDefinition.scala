package com.voombua.repos

import java.sql.Timestamp
import java.util.UUID

import com.voombua.models.Entity
import slick.jdbc.JdbcProfile
import slick.lifted

import scala.concurrent.{ ExecutionContext, Future }
import scala.reflect.ClassTag

/**
 * The root DB component which provides information
 * how a driver has to be look -> Just a JDBC Profile
 */
trait DB {

  val driver: JdbcProfile

  import driver.api._

  lazy val db: Database = Database.forConfig("database")
}

/**
 * The final H2 in memory implementation which we can mixin
 */
trait H2 extends DB {
  override val driver: JdbcProfile = slick.jdbc.H2Profile
}

/**
 * The final Postgres DB implementation which we can mixin
 */
trait PG extends DB {
  override val driver: JdbcProfile = slick.jdbc.PostgresProfile
}

/**
 * The root table definition which defines how a table has to be look like
 */
trait TableDefinition { this: DB =>

  import driver.api._

  /**
   * The [[BaseTable]] describes the basic [[Entity]]
   */
  abstract class BaseTable[E <: Entity: ClassTag](
    tag: Tag,
    tableName: String,
    schemaName: Option[String] = None
  )
      extends Table[E](tag, schemaName, tableName) {

    val id = column[String]("id", O.PrimaryKey)
    val created = column[Timestamp]("created_at")
    val updated = column[Timestamp]("updated_at")
    val deleted = column[Timestamp]("deleted_at")
  }
}

sealed trait Repository[E <: Entity] {
  def all: Future[Seq[E]]
  def byId(id: String): Future[Option[E]]
  def insert(entity: E): Future[E]
  def update(entity: E): Future[Int]
  def delete(id: String): Future[Boolean]
}

/**
 * The root basic repository definition
 * The repo definition which should be used within an entity repo
 */
trait RepoDefinition extends TableDefinition { this: DB =>

  import driver.api._

  abstract class BaseRepo[E <: Entity, T <: BaseTable[E]](implicit ex: ExecutionContext)
      extends Repository[E] {

    val table: lifted.TableQuery[T]

    override def all: Future[Seq[E]] = db.run {
      table.to[Seq].result
    }

    override def byId(id: String): Future[Option[E]] = db.run {
      table.filter(_.id === id).result.headOption
    }

    override def insert(entity: E): Future[E] = db.run {
      table returning table += entity
    }

    override def update(entity: E): Future[Int] = db.run {
      table.insertOrUpdate(entity)
    }

    override def delete(id: String): Future[Boolean] = db.run {
      table.filter(_.id === id).delete.map(_ > 0)
    }
  }
}

