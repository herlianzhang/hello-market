package com.dpr.hello_market.db.activity

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ActivityDao {
    @Query("SELECT * FROM activity_table ORDER BY id DESC")
    fun getActivity(): LiveData<List<ActivityDbModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivity(activity: ActivityDbModel)
}