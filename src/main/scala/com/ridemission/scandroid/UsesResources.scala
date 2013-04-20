package com.ridemission.scandroid

import android.content.Context
import android.widget.Toast

/**
 * Mixin for easier resource access
 */
trait UsesResources {
  def context: Context

  lazy val resources = context.getResources

  final def S(id: Int) = resources.getString(id)

  final def toast(str: String, isLong: Boolean = false) {
    Toast.makeText(context, str, if (isLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT).show()
  }

  final def toast(str: Int, isLong: Boolean) {
    Toast.makeText(context, str, if (isLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT).show()
  }

}