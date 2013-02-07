package com.ridemission.scandroid

import _root_.android.os.Bundle
import android.content.Intent
import android.widget._
import android.content.ServiceConnection
import android.content.ComponentName
import android.os.IBinder
import android.content.Context
import android.content.res.Configuration
import android.os.Handler
import scala.language.postfixOps
import android.hardware.usb.UsbManager
import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.view.View
import android.view.Menu
import android.widget.AdapterView.OnItemSelectedListener
import android.view.MenuItem
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.app.FragmentActivity
import android.support.v4.view.ViewPager
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.ViewGroup

case class PageInfo(title: String, generator: () => Fragment) {
  override def toString = title
}

/**
 * FIXME
 * We might want to use a version of the pager adapter that destroys fragments when not in use (so we don't spend cycles updating RC channels when not visible)
 *
 */
class ScalaPagerAdapter(fm: FragmentManager, private var pages: IndexedSeq[PageInfo]) extends FragmentPagerAdapter(fm) {

  private var curPage: Option[PagerPage] = None

  def setPages(p: IndexedSeq[PageInfo]) = {
    pages = p
    notifyDataSetChanged()
  }

  override def getItem(position: Int) = {
    // getItem is called to instantiate the fragment for the given page.
    // Return a DummySectionFragment (defined as a static inner class
    // below) with the page number as its lone argument.
    val fragment = pages(position).generator()
    //Bundle args = new Bundle();
    //args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
    //fragment.setArguments(args);
    fragment
  }

  override def getCount() = pages.size

  override def getPageTitle(i: Int) = pages(i).title

  override def setPrimaryItem(container: ViewGroup, position: Int, obj: Object) {
    super.setPrimaryItem(container, position, obj)

    // If the fragment doesn't care to be notified of extra page stuff - don't bother with it
    val newPage = if (obj.isInstanceOf[PagerPage]) Some(obj.asInstanceOf[PagerPage]) else None

    if (curPage != newPage) { // Android seems to send redundant notifications - don't get confused
      curPage.foreach(_.onPageHidden())
      curPage = newPage
      newPage.foreach(_.onPageShown())
    }
  }
}
