package com.ridemission.scandroid

import android.content.Context
import android.widget.Toast

/**
 * Mixin for easier resource access
 */
trait UsesResources {
  def acontext: Context

  lazy val resources = acontext.getResources

  final def S(id: Int) = resources.getString(id)

  final def toast(str: String, isLong: Boolean = false) {
    Toast.makeText(acontext, str, if (isLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT).show()
  }

  final def toast(str: Int, isLong: Boolean) {
    Toast.makeText(acontext, str, if (isLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT).show()
  }

}