package com.ridemission.scandroid

import android.content._
import android.database._
import android.database.sqlite._

case class TableDefinition(val name: String, val columnDefs: String)

/// Provides SQLiteOpenHelper access to SQLite databases (for auto upgrading etc...)
/// @param dbVersion The android version # we can accept without needing to upgrade DB
trait SQLiteWrapper extends AndroidLogger {
  /// Must be provided by subclass
  def getContext: Context

  /// Must be provided by subclass
  def databaseName: String

  /// Must be provided by subclass
  def dbVersion: Int

  /// Must be provided by subclass
  def tableDefs: Seq[TableDefinition]

  private lazy val dbHelper = new SQLiteOpenHelper(getContext, databaseName, null, dbVersion) {
    override def onCreate(db: SQLiteDatabase) {
      tableDefs.foreach { t => db.execSQL("create table " + t.name + "(" + t.columnDefs + ");") }
    }

    override def onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
      if (oldVersion >= dbVersion)
        debug("Skipping DB upgrade, schema has not changed")
      else
        updateSchema(oldVersion, newVersion)
    }
  }

  lazy val db = dbHelper.getWritableDatabase

  /// Close the database
  def close() { db.close() }

  /// Override this method to update old databases to new database versions
  protected def updateSchema(oldVersion: Int, newVersion: Int) {
    warn("FIXME - add db schema update code (alter table foo...)")
  }
}
