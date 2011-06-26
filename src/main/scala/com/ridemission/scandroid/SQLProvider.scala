package com.ridemission.scandroid

import android.net._
import android.content._
import android.database._
import android.database.sqlite._
import android.provider._

/// A ContentProvider backed by an sql database
abstract class SQLProvider(val databaseName: String, authority: String) extends ContentProvider with SQLiteWrapper {
  /// a mapping from table name to the type of content at that path
  /// Subclass must provide
  /// Our URIs are formatted as content:://authority/tablename
  def tableNameToContentType: Map[String, String]

  lazy val tableNames = tableNameToContentType.keys.toSeq

  def resolver = getContext.getContentResolver

  val key_id = "_id"

  private val uriMatcher = new UriMatcher(UriMatcher.NO_MATCH)

  override def onCreate() = {
    db // Reference our lazily created db to force create

    tableNames.zipWithIndex.foreach { pair => uriMatcher.addURI(authority, pair._1, pair._2) }

    true
  }

  override def query(uri: Uri, projection: Array[String], selection: String, selectionArgs: Array[String], sortOrder: String) = {
    val table = uriToTable(uri)
    val qb = new SQLiteQueryBuilder
    qb.setTables(table)

    // FIXME support matching URIs of the form "friends/#" for finding
    // by ID #
    // If the query ends in a specific record number, we're
    // being asked for a specific record, so set the
    // WHERE clause in our query.
    /* if((URI_MATCHER.match(uri)) == SPECIFIC_MESSAGE){
            qBuilder.appendWhere("_id=" + uri.getPathLeafId());
        } */

    // Make the query.
    val cursor = qb.query(db,
      projection,
      selection,
      selectionArgs,
      null,
      null,
      sortOrder);
    cursor.setNotificationUri(resolver, uri);
    cursor
  }

  override def getType(uri: Uri): String = tableNameToContentType(uriToTable(uri))

  override def insert(uri: Uri, values: ContentValues): Uri = {
    val table = uriToTable(uri)
    val rowId = db.insert(table, key_id, values)

    if (rowId < 0)
      throw new SQLException("Failed to insert row into " + uri)

    val newuri = rowToUri(table, rowId)
    resolver.notifyChange(newuri, null)
    newuri
  }

  override def delete(uri: Uri, selection: String, selectionArgs: Array[String]): Int = {
    val table = uriToTable(uri)
    val numdel = db.delete(table, selection, selectionArgs)

    if (numdel > 0)
      resolver.notifyChange(uri, null)

    numdel
  }

  override def update(uri: Uri, values: ContentValues, selection: String, selectionArgs: Array[String]): Int = {
    val table = uriToTable(uri)
    val numchange = db.update(table, values, selection, selectionArgs)

    if (numchange > 0)
      resolver.notifyChange(uri, null)

    numchange
  }

  /// Generate a URI for this row
  private def rowToUri(table: String, rowId: Long) =
    ContentUris.withAppendedId(tableToUri(table), rowId)

  /// Generate a URI for this table
  private def tableToUri(table: String) = Uri.parse("content://" + authority + "/" + table)

  /// Find the correct table for this URI
  def uriToTable(uri: Uri): String = {
    val loc = uriMatcher.`match`(uri)
    if (loc == -1)
      throw new IllegalArgumentException("Unknown URI " + uri)

    tableNames(loc)
  }

}
