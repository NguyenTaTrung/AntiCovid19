package com.example.googlemap.di

import androidx.room.Room
import com.example.googlemap.data.resource.*
import com.example.googlemap.data.resource.local.InformationLocalDataSource
import com.example.googlemap.data.resource.local.TimeLocalDataSource
import com.example.googlemap.data.resource.local.db.MyDatabase
import com.example.googlemap.data.resource.remote.InformationRemoteDataSource
import com.example.googlemap.data.resource.repository.InformationRepository
import com.example.googlemap.data.resource.repository.LocationRepository
import com.example.googlemap.data.resource.repository.TimeRepository
import com.example.googlemap.utils.SharedPreferencesHelper
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val repoModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            MyDatabase::class.java,
            "my_database"
        ).build()
    }

    single { get<MyDatabase>().informationDao() }
}

val informationRepo = module {
    single { SharedPreferencesHelper(get()) }
    single<InformationDataSource.Remote> {
        InformationRemoteDataSource(
            get(named("ApiService")),
            get(named("ApiCovid")),
            get(named("ApiRss"))
        )
    }
    single<InformationDataSource.Local> { InformationLocalDataSource(get(), get()) }

    single {
        InformationRepository(
            get(),
            get()
        )
    }
}

val locationRepo = module {
    single { MyLocationManager(get(), get()) }
    single {
        LocationRepository(
            get()
        )
    }
}

val timeRepo = module {
    single { AlarmManager(get()) }
    single<TimeDataSource.Local> { TimeLocalDataSource(get(), get()) }
    single { TimeRepository(get()) }
}
