package com.dpr.hello_market.db.cart

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CartDbModel::class], version = 1, exportSchema = false)
abstract class CartDatabase: RoomDatabase() {
    abstract fun cartDao(): CartDao
}