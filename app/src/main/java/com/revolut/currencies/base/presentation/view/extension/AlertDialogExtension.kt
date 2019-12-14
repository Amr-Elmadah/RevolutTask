package com.revolut.currencies.base.presentation.view.extension


import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog

fun AlertDialog.Builder.createAlertWithPositiveButton(
    title: String = "", message: String, positiveText: String,
    positiveButtonListener: DialogInterface.OnClickListener
) {

    setTitle(title)
    setMessage(message)
    setPositiveButton(positiveText, positiveButtonListener)
    val dialog = create()
    dialog.show()
}

fun AlertDialog.Builder.createAlertWithTwoButtons(
    title: String = "", message: String, positiveText: String,
    negativeText: String, positiveButtonListener: DialogInterface.OnClickListener
) {
    setTitle(title)
    setMessage(message)
    setPositiveButton(positiveText, positiveButtonListener)
    setNegativeButton(negativeText) { _, _ -> }
    val dialog = create()
    dialog.show()
}