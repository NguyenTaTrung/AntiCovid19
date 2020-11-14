package com.example.googlemap.data.resource.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.googlemap.data.model.Information
import com.example.googlemap.data.resource.local.dao.InformationDao

@Database(entities = [Information::class], version = 1)
abstract class MyDatabase : RoomDatabase() {
    abstract fun informationDao(): InformationDao
}
