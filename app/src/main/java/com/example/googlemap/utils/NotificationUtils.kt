package com.example.googlemap.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.googlemap.R

object NotificationUtils {
    private const val CHANNEL_ID = "channel_01"
    private const val NOTIFICATION_ID = 15

    fun create(
        context: Context,
        title: String,
        msg: String,
        icon: Int,
        pendingIntent: PendingIntent
    ) {
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)

        builder.setSmallIcon(R.drawable.img_logo_app)
            .setLargeIcon(
                BitmapFactory.decodeResource(context.resources, icon)
            )
            .setContentTitle(title)
            .setContentText(msg)
            .setContentIntent(pendingIntent)

        builder.setAutoCancel(true)

        val mNotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = context.getString(R.string.app_name)
            val mChannel =
                NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT)

            mNotificationManager.createNotificationChannel(mChannel)

            builder.setChannelId(CHANNEL_ID)
        }

        mNotificationManager.notify(NOTIFICATION_ID, builder.build())
    }
}
