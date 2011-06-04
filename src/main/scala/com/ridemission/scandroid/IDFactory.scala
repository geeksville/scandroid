package com.ridemission.scandroid

/// A little helper class that just generates unique integers
class IDFactory {
  private var n = 0

  def create = {
    n += 1
    n
  }
}
