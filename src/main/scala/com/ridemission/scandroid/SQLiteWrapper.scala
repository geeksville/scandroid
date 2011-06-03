package com.ridemission.scandroid

import android.content._
import android.database._
import android.database.sqlite._

case class TableDefinition(val name: String, val columnDefs: String)

/// @param dbVersion The android version # we can accept without needing to upgrade DB
class SQLiteWrapper(context: Context, val databaseName: String, val dbVersion: Int, val tableDefs: Seq[TableDefinition]) extends AndroidLogger {

  private val dbHelper = new SQLiteOpenHelper(context, databaseName, null, dbVersion) {
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

  val db = dbHelper.getWritableDatabase

  /// Close the database
  def close() { db.close() }

  /// Override this method to update old databases to new database versions
  protected def updateSchema(oldVersion: Int, newVersion: Int) {
    warn("FIXME - add db schema update code (alter table foo...)")
  }
}
