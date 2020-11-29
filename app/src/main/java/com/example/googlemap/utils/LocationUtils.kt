package com.example.googlemap.utils

import androidx.fragment.app.DialogFragment
import com.google.android.material.snackbar.Snackbar

fun DialogFragment.requestPermissionWithRationale(
    permission: String,
    requestCode: Int,
    snackBar: Snackbar
) {
    val provideRationale = shouldShowRequestPermissionRationale(permission)

    if (provideRationale) {
        snackBar.show()
    } else {
        requestPermissions(arrayOf(permission), requestCode)
    }
}
