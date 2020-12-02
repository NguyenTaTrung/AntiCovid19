package com.example.googlemap.data.resource.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.googlemap.data.resource.AlarmManager
import com.example.googlemap.data.resource.TimeDataSource
import com.example.googlemap.utils.PreferencesConst.PREFS_IS_ALLOW_NOTIFICATION
import com.example.googlemap.utils.SharedPreferencesHelper

class TimeLocalDataSource(
    private val alarmManager: AlarmManager,
    private val preferencesHelper: SharedPreferencesHelper
) : TimeDataSource.Local {

    private val _isAllowNotification = MutableLiveData<Boolean>(preferencesHelper[PREFS_IS_ALLOW_NOTIFICATION, Boolean::class.java])
    override val isAllowNotification: LiveData<Boolean>
        get() = _isAllowNotification

    override fun createAlarm() = alarmManager.create()

    override fun cancelAlarm() = alarmManager.cancel()

    override fun updateNotification(isAllowNotification: Boolean) {
        preferencesHelper.put(PREFS_IS_ALLOW_NOTIFICATION, isAllowNotification)
        _isAllowNotification.value = isAllowNotification
    }
}
