package com.dpr.hello_market.db.activity

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [ActivityDbModel::class], version = 1, exportSchema = false)
@TypeConverters(ActivityProductConverter::class)
abstract class ActivityDatabase : RoomDatabase() {
    abstract fun activityDao(): ActivityDao
}