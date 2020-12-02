package com.example.googlemap.data.resource.repository

import androidx.lifecycle.LiveData
import com.example.googlemap.data.resource.TimeDataSource

class TimeRepository(
    private val local: TimeDataSource.Local
) : TimeDataSource.Local {
    override val isAllowNotification: LiveData<Boolean>
        get() = local.isAllowNotification

    override fun createAlarm() = local.createAlarm()

    override fun cancelAlarm() = local.cancelAlarm()

    override fun updateNotification(isAllowNotification: Boolean) = local.updateNotification(isAllowNotification)
}
