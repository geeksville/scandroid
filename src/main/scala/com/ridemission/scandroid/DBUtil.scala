package com.ridemission.scandroid

import android.content._

object DBUtil {
  /// Helper to convert scala Maps to android ContentValues
  implicit def toContentValues[T](vals: scala.collection.Map[String, T]) = {
    val result = new ContentValues
    vals.foreach { pair =>
      val key = pair._1
      pair._2 match {
        case v: Byte => result.put(key, v: java.lang.Byte)
        case v: Int => result.put(key, v: java.lang.Integer)
        case v: Float => result.put(key, v: java.lang.Float)
        case v: Double => result.put(key, v: java.lang.Double)
        case v: Long => result.put(key, v: java.lang.Long)
        case v: Array[Byte] => result.put(key, v)
        case v: String => result.put(key, v)
        case v: Boolean => result.put(key, v)
      }
    }
    result
  }
}
