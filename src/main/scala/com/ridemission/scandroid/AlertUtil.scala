package com.ridemission.scandroid

import android.app.AlertDialog
import android.app.Activity
import AndroidUtil._
import android.support.v4.app.DialogFragment
import android.os.Bundle
import android.support.v4.app.Fragment
import android.content.DialogInterface
import android.widget.Button
import android.support.v4.app.FragmentActivity

class SimpleOkayDialog(prompt: String, onOk: () => Unit, icon: Int = -1) extends DialogFragment {
  override def onCreateDialog(savedInstanceState: Bundle) = {
    val builder = new AlertDialog.Builder(getActivity)

    // 2. Chain together various setter methods to set the dialog characteristics
    if (icon != -1)
      builder.setIcon(icon)
    builder.setMessage(prompt)
    builder.setPositiveButton("OK", onOk)
    //   .setTitle(R.string.dialog_title);

    // 3. Get the AlertDialog from create()
    builder.create()
  }

  override def onDismiss(i: DialogInterface) {
    super.onDismiss(i)
    onOk()
  }
}

object SimpleOkayDialog {
  /**
   * Ask OK or cancel for a question...
   */
  def show(activity: FragmentActivity, prompt: String, icon: Int = -1, onOk: () => Unit = { () => }, name: String = "okay") {
    val newFragment = new SimpleOkayDialog(prompt, onOk, icon)

    newFragment.show(activity.getSupportFragmentManager(), name)
  }
}

class SimpleYesNoDialog(prompt: String, onOk: () => Unit, onCancel: () => Unit) extends DialogFragment {
  override def onCreateDialog(savedInstanceState: Bundle) = {
    val builder = new AlertDialog.Builder(getActivity)

    // 2. Chain together various setter methods to set the dialog characteristics
    builder.setMessage(prompt)
    builder.setPositiveButton("OK", onOk)
    builder.setNegativeButton("Cancel", onCancel)
    //   .setTitle(R.string.dialog_title);

    // 3. Get the AlertDialog from create()
    builder.create()
  }

  override def onDismiss(i: DialogInterface) {
    super.onDismiss(i)
    onCancel()
  }
}

/// A mixin to make it easy to raise simple dialogs
trait SimpleDialogClient extends Fragment {

  /**
   * Ask OK or cancel for a question...
   */
  def showYesNo(prompt: String, onOk: () => Unit, onCancel: () => Unit = () => {}, name: String = "yesno") {
    val newFragment = new SimpleYesNoDialog(prompt, onOk, onCancel)

    newFragment.show(getActivity.getSupportFragmentManager(), name)
  }

  /**
   * Add a second confirmation dialog to a button press
   */
  def confirmButtonPress(button: Button, prompt: String, name: String = "yesno")(onOk: () => Unit) {
    button.onClick { b =>
      showYesNo(prompt, onOk)
    }
  }
}