package com.example.googlemap.broadcast

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.googlemap.R
import com.example.googlemap.data.model.CaseInformation
import com.example.googlemap.ui.main.MainActivity
import com.example.googlemap.utils.NotificationUtils
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
                                NotificationUtils.create(
                                    context,
                                    context.getString(R.string.title_notification_location_update),
                                    context.getString(R.string.msg_notification_location_update, listCaseInformation.size),
                                    R.drawable.ic_warning,
                                    getPendingIntent(context)
                                )
                                putValueSharePreferences(it.id)
                            }
                        }
                        sharedPreferences = null
                    }
                }
            }
        }
    }

    private fun getPendingIntent(context: Context): PendingIntent {
        val notificationIntent = Intent(context, MainActivity::class.java)
        return PendingIntent.getActivity(context, 0, notificationIntent, 0)
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
        const val ACTION_PROCESS_UPDATES =
            "com.google.android.gms.location.sample.locationupdatesbackgroundkotlin.action." +
                    "PROCESS_UPDATES"
    }
}
