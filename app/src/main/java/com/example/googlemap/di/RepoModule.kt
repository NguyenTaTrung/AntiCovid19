package com.example.googlemap.di

import androidx.room.Room
import com.example.googlemap.data.resource.InformationDataSource
import com.example.googlemap.data.resource.InformationRepository
import com.example.googlemap.data.resource.local.db.MyDatabase
import com.example.googlemap.data.resource.remote.InformationRemoteDataSource
import org.koin.android.ext.koin.androidContext
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
    single<InformationDataSource.Remote> { InformationRemoteDataSource(get()) }

    single { InformationRepository(get()) }
}
