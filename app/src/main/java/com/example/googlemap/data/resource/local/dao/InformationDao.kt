package com.example.googlemap.data.resource.local.dao

import androidx.room.*
import com.example.googlemap.data.resource.local.entity.Information
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

@Dao
interface InformationDao {
    @Query("SELECT * FROM information ORDER BY id DESC LIMIT 1")
    fun getLastInformation(): Flowable<Information>

    @Query("SELECT COUNT(*) FROM information")
    fun informationCount(): Single<Int>

    @Insert
    fun addInformation(information: Information): Completable

    @Update
    fun updateInformation(information: Information): Completable
}
