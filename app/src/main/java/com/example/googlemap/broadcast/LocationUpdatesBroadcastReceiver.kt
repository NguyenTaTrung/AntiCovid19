package com.example.googlemap.broadcast

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.googlemap.R
import com.example.googlemap.data.model.CaseInformation
import com.example.googlemap.ui.main.MainActivity
import com.example.googlemap.utils.PreferencesConst.PREFS_ID_COVID_CASE_INFORMATION
import com.example.googlemap.utils.SharedPreferencesHelper
import com.google.android.gms.location.LocationResult
import org.json.JSONArray
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class LocationUpdatesBroadcastReceiver : BroadcastReceiver() {

    private var sharedPreferences: SharedPreferencesHelper? = null

    override fun onReceive(context: Context, intent: Intent) {
        sharedPreferences = SharedPreferencesHelper(context)
        val list = mutableListOf<CaseInformation>()
        val json = JSONArray(intent.data.toString())
        for (i in 0 until json.length()) {
            val jsonObject = json.getJSONObject(i)
            list.add(CaseInformation(jsonObject))
        }

        if (intent.action == ACTION_PROCESS_UPDATES) {
            LocationResult.extractResult(intent)?.let { locationResult ->
                locationResult.locations.map { location ->
                    val listCaseInformation = list.filter {
                        calculator(location.latitude, location.longitude, it.lat, it.lon).toInt() < 1
                    }.toMutableList()

                    if (listCaseInformation.isNotEmpty()) {
                        listCaseInformation.forEach {
                            if (getValueSharePreferences(it.id) != it.id) {
                                sendNotification(context, listCaseInformation.size)
                                putValueSharePreferences(it.id)
                            }
                        }
                        sharedPreferences = null
                    }
                }
            }
        }
    }

    private fun sendNotification(context: Context, count: Int) {
        val notificationIntent = Intent(context, MainActivity::class.java)
        //notificationIntent.putExtra("from_notification", true)

        val notificationPendingIntent =
            PendingIntent.getActivity(context, 0, notificationIntent, 0)

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)

        builder.setSmallIcon(R.mipmap.ic_launcher)
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    context.resources,
                    R.mipmap.ic_launcher
                )
            )
            .setColor(Color.RED)
            .setContentTitle("Location update")
            .setContentText("Cảnh báo có $count người nhiễm bệnh trong khu vực bạn sinh sống")
            .setContentIntent(notificationPendingIntent)

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

    private fun calculator(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val r = 6371
        val phi1 = lat1.times(Math.PI.div(180))
        val phi2 = lat2.times(Math.PI.div(180))
        val deltaPhi = (lat2.minus(lat1)).times(Math.PI.div(180))
        val deltaLambda = (lon2.minus(lon1)).times(Math.PI.div(180))
        val a = (sin(deltaPhi.div(2)).times(sin(deltaPhi.div(2)))).plus(
            (cos(phi1).times(cos(phi2)).times(
                sin(deltaLambda.div(2))
            ).times(sin(deltaLambda.div(2))))
        )
        val c = atan2(sqrt(a), sqrt(1 - a)).times(2)
        return r.times(c)
    }

    private fun putValueSharePreferences(id: Int) {
        sharedPreferences?.put(PREFS_ID_COVID_CASE_INFORMATION + "$id", id)
    }

    private fun getValueSharePreferences(id: Int): Int? =
        sharedPreferences?.get(PREFS_ID_COVID_CASE_INFORMATION + "$id", Int::class.java)

    companion object {
        private const val CHANNEL_ID = "channel_01"
        private const val NOTIFICATION_ID = 15
        const val ACTION_PROCESS_UPDATES =
            "com.google.android.gms.location.sample.locationupdatesbackgroundkotlin.action." +
                    "PROCESS_UPDATES"
    }
}
