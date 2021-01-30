package com.dpr.hello_market.db.activity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.dpr.hello_market.db.cart.CartDbModel

@Entity(tableName = "activity_table")
data class ActivityDbModel(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @TypeConverters(ActivityProductConverter::class) var order: List<CartDbModel>
)