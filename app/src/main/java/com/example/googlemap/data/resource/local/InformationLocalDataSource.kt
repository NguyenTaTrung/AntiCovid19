package com.example.googlemap.data.resource.local

import com.example.googlemap.data.resource.InformationDataSource
import com.example.googlemap.utils.PreferencesConst.PREFS_IS_VIETNAMESE_LANGUAGE
import com.example.googlemap.utils.SharedPreferencesHelper

class InformationLocalDataSource(
    private val preferencesHelper: SharedPreferencesHelper
) : InformationDataSource.Local {

    override fun getLanguage(): Boolean {
        return preferencesHelper[PREFS_IS_VIETNAMESE_LANGUAGE, Boolean::class.java]
    }

    override fun updateLanguage(isVietnamese: Boolean) {
        preferencesHelper.put(PREFS_IS_VIETNAMESE_LANGUAGE, isVietnamese)
    }
}
