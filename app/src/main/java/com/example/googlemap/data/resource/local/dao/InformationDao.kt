package com.example.googlemap.data.resource.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.googlemap.data.model.Information

@Dao
interface InformationDao {
    @Query("SELECT * FROM information ORDER BY id DESC LIMIT 1")
    fun getLastInformation(): LiveData<Information>

    @Insert
    fun addInformation(information: Information)

    @Update
    fun updateInformation(information: Information)
}
