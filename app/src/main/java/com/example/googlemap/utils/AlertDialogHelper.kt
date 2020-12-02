package com.example.googlemap.utils

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import com.example.googlemap.R

class AlertDialogHelper(
    private val context: Context,
    private val positiveClickListener: (DialogInterface, Int) -> Unit
) {
    private val builder = AlertDialog.Builder(context)

    fun createDialog(title: String, message: String): AlertDialog {
        return builder
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(context.getString(R.string.text_agree), positiveClickListener)
            .setNegativeButton(context.getString(R.string.text_diagree)) { _, _ -> }
            .create()
    }
}
