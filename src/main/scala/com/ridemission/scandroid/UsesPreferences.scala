package com.ridemission.scandroid

import android.content.Context
import android.preference.PreferenceManager
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.content.SharedPreferences

/**
 * A mixin for easier preference access
 */
trait UsesPreferences { context: Context =>
  lazy val preferences = PreferenceManager.getDefaultSharedPreferences(this)

  def stringPreference(s: String, defVal: String = "") = tryOrElse(preferences.getString(s, defVal), defVal)
  def intPreference(s: String, defVal: Int) = tryOrElse(preferences.getInt(s, defVal), defVal)
  def boolPreference(s: String, defVal: Boolean) = tryOrElse(preferences.getBoolean(s, defVal), defVal)

  def tryOrElse[T](f: => T, elseVal: T) = try {
    f
  } catch {
    case ex: Exception => elseVal
  }

  /**
   * Call the callback when the named preference changes (make sure to unregister later)
   */
  def registerOnPreferenceChanged(prefName: String)(cb: () => Unit) = {
    val l = new OnSharedPreferenceChangeListener {
      def onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        if (key == prefName)
          cb()
      }
    }
    preferences.registerOnSharedPreferenceChangeListener(l)
    l
  }

  def unregisterOnPreferenceChanged(l: OnSharedPreferenceChangeListener) { preferences.unregisterOnSharedPreferenceChangeListener(l) }
}