package com.dpr.hello_market.db.activity

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ActivityDbModel::class], version = 1, exportSchema = false)
abstract class ActivityDatabase : RoomDatabase() {
    abstract fun activityDao(): ActivityDao
}