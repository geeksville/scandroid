package com.ridemission.scandroid

import scala.util.logging._

import android.util.Log

/// extend this trait to get all sorts of easy logging wrappers
/// FIXME - turn off logging when debugging is off
trait AndroidLogger {
  /// The tag string used for all our messages
  private lazy val tag = getClass.getSimpleName

  /// Is anyone even interested in looking at these log msgs
  /// Use adb shell setprop log.tag.scandroid VERBOSE
  // private def isLoggable(lvl: Int) = Log.isLoggable(enableTag, lvl)
  private final def isLoggable(lvl: Int) = AndroidLogger.enable

  final def info(msg: => String) = if (isLoggable(Log.INFO)) Log.i(tag, msg)
  final def debug(msg: => String) = if (isLoggable(Log.DEBUG)) Log.d(tag, msg)
  final def error(msg: => String) = if (isLoggable(Log.ERROR)) Log.e(tag, msg)
  final def warn(msg: => String) = if (isLoggable(Log.WARN)) Log.w(tag, msg)

  // Not available in android-2.1/7
  // def wtf(msg: String) = Log.wtf(tag, msg)
  // def wtf(msg: String, ex: Throwable) = Log.wtf(tag, msg, ex)
}

object AndroidLogger {
  var enable = true
}