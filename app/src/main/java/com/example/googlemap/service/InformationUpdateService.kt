package com.example.googlemap.service

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.JobIntentService
import com.example.googlemap.R
import com.example.googlemap.data.model.Country
import com.example.googlemap.data.resource.local.entity.Information
import com.example.googlemap.data.resource.repository.InformationRepository
import com.example.googlemap.ui.main.MainActivity
import com.example.googlemap.utils.NotificationUtils
import com.example.googlemap.utils.showToast
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.schedulers.Schedulers.io
import org.koin.android.ext.android.inject

class InformationUpdateService : JobIntentService() {

    private val repo by inject<InformationRepository>()

    override fun onHandleWork(intent: Intent) {
        Observable.combineLatest(
            repo.getSummaryData().flatMap { summary ->
                Observable.fromIterable(summary.countries)
                    .filter { it.country == "Viet Nam" }
            },
            repo.getLastInformation().toObservable(),
            BiFunction<Country, Information, Information?> { country, information ->
                var newInformation: Information? = null
                if (country.totalConfirmed != information.totalConfirmed ||
                    country.totalDeaths != information.totalDeaths ||
                    country.totalRecovered != information.totalRecovered
                ) {
                    newInformation =
                        Information(
                            information?.id,
                            country.newConfirmed,
                            country.totalConfirmed,
                            country.newDeaths,
                            country.totalDeaths,
                            country.newRecovered,
                            country.totalRecovered
                        )
                    showNotification(country)
                }
                newInformation
            }
        )
            .subscribeOn(io())
            .flatMapCompletable {
                it?.let { repo.updateInformation(it) }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({}, {
                showToast(it.message.toString())
            })
    }

    private fun showNotification(country: Country) {
        NotificationUtils.create(
            this,
            getString(R.string.title_notification),
            getString(R.string.text_head_notification, country.newConfirmed) +
                    getString(R.string.text_body_notification, country.newDeaths) +
                    getString(R.string.text_end_notification, country.newRecovered),
            R.drawable.img_logo_app,
            getPendingIntent()
        )
    }

    private fun getPendingIntent(): PendingIntent {
        val intent = Intent(this, MainActivity::class.java)
        return PendingIntent.getActivity(this, 0, intent, 0)
    }

    companion object {
        private const val JOB_ID = 55
        fun enqueueWork(context: Context, work: Intent) {
            enqueueWork(context, InformationUpdateService::class.java, JOB_ID, work)
        }
    }
}
