package com.example.googlemap.data.resource

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import com.example.googlemap.broadcast.InformationUpdatesBroadcastReceiver

class AlarmManager(
    private val context: Context
) {
    fun create() {
        val intent = Intent(context, InformationUpdatesBroadcastReceiver::class.java)
        val alarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        val pendingIntent =
            PendingIntent.getBroadcast(context, 20, intent, 0)
        alarmManager?.setInexactRepeating(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_HALF_HOUR,
            AlarmManager.INTERVAL_HALF_HOUR,
            pendingIntent
        )
    }

    fun cancel() {
        val alarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        val intent = Intent(context, InformationUpdatesBroadcastReceiver::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(context, 20, intent, 0)
        alarmManager?.cancel(pendingIntent)
    }
}
