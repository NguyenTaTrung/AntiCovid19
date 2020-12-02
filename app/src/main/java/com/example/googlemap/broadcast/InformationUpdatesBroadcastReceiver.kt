package com.example.googlemap.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.googlemap.service.InformationUpdateService

class InformationUpdatesBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, work: Intent) {
        if (work.action == "android.intent.action.BOOT_COMPLETED") {

        }
        InformationUpdateService.enqueueWork(context, work)
    }
}
