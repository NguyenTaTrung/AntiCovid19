package com.example.googlemap.data.resource

import androidx.lifecycle.LiveData

interface TimeDataSource {
    interface Local {
        val isAllowNotification: LiveData<Boolean>
        fun createAlarm()
        fun cancelAlarm()
        fun updateNotification(isAllowNotification: Boolean)
    }
}
