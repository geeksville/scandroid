package com.ridemission.scandroid

import android.app._
import android.os._
import android.content._

import scala.collection.mutable.ListMap

/// A scala friendly api for starting activities and running code when
/// they complete.
trait ActivityClient extends Activity with AndroidLogger {
  type ActivityResultCallback = (Int, Intent) => Unit

  private val nextId = new IDFactory
  private val ids = new ListMap[Int, ActivityResultCallback]

  override def onActivityResult(reqCode: Int, resultCode: Int, data: Intent) {
    val callback = ids(reqCode)

    ids -= reqCode // Done processing this activity
    callback(resultCode, data)
  }

  /// Starts the given activity, when the result arrives the callback
  /// will be called
  def startActivityForResult(intent: Intent)(callback: ActivityResultCallback) {
    val id = nextId.create
    ids += id -> callback
    startActivityForResult(intent, id)
  }
}
