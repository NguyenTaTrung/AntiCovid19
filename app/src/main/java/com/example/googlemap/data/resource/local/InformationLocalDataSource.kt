package com.example.googlemap.data.resource.local

import com.example.googlemap.data.resource.local.entity.Information
import com.example.googlemap.data.resource.InformationDataSource
import com.example.googlemap.data.resource.local.dao.InformationDao
import com.example.googlemap.utils.PreferencesConst.PREFS_IS_VIETNAMESE_LANGUAGE
import com.example.googlemap.utils.SharedPreferencesHelper
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

class InformationLocalDataSource(
    private val informationDao: InformationDao,
    private val preferencesHelper: SharedPreferencesHelper
) : InformationDataSource.Local {

    override fun getLanguage(): Boolean =
        preferencesHelper[PREFS_IS_VIETNAMESE_LANGUAGE, Boolean::class.java]

    override fun updateLanguage(isVietnamese: Boolean) {
        preferencesHelper.put(PREFS_IS_VIETNAMESE_LANGUAGE, isVietnamese)
    }

    override fun getLastInformation(): Flowable<Information> = informationDao.getLastInformation()

    override fun informationCount(): Single<Int> = informationDao.informationCount()

    override fun addInformation(information: Information): Completable =
        informationDao.addInformation(information)

    override fun updateInformation(information: Information): Completable =
        informationDao.updateInformation(information)
}
