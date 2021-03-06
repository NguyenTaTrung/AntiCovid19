package com.example.googlemap.utils

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.hasPermission(permission: String): Boolean {
    // Background permissions didn't exit prior to Q, so it's approved by default.
    if (permission == Manifest.permission.ACCESS_BACKGROUND_LOCATION &&
        android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.Q) {
        return true
    }

    return ActivityCompat.checkSelfPermission(this, permission) ==
            PackageManager.PERMISSION_GRANTED
}

fun Context.alertDialog(
    title: String,
    message: String,
    positiveClickListener: (DialogInterface, Int) -> Unit
): AlertDialog {
    return AlertDialogHelper(this, positiveClickListener).createDialog(title, message)
}
