package com.example.googlemap.data.resource.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "information")
data class Information (
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    @ColumnInfo(name = "new_confirmed")
    val newConfirmed: Int,
    @ColumnInfo(name = "total_confirmed")
    val totalConfirmed: Int,
    @ColumnInfo(name = "new_deaths")
    val newDeaths: Int,
    @ColumnInfo(name = "total_deaths")
    val totalDeaths: Int,
    @ColumnInfo(name = "new_recovered")
    val newRecovered: Int,
    @ColumnInfo(name = "total_recovered")
    val totalRecovered: Int
)
