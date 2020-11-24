package com.example.googlemap.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.example.googlemap.R
import com.example.googlemap.utils.PreferencesConst.PREFS_NAME
import java.lang.ClassCastException
import kotlin.jvm.Throws

class SharedPreferencesHelper(context: Context) {
    private var sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE)

    fun <T> put(key: String, data: T) {
        val editor = sharedPreferences.edit()
        when (data) {
            is String -> editor.putString(key, data)
            is Boolean -> editor.putBoolean(key, data)
            is Int -> editor.putInt(key, data)
        }
        editor.apply()
    }

    @Suppress("UNCHECKED_CAST")
    @Throws(ClassCastException::class)
    operator fun <T> get(key: String, anonymousClass: Class<T>): T = when (anonymousClass) {
        String::class.java -> sharedPreferences.getString(key, null) as T
        Boolean::class.java -> sharedPreferences.getBoolean(key, true) as T
        Int::class.java -> sharedPreferences.getInt(key, 0) as T
        else -> throw ClassCastException(R.string.error_cash_class.toString())
    }

    fun clear() = sharedPreferences.edit().clear().apply()
}
