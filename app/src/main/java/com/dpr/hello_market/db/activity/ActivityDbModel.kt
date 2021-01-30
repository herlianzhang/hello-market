package com.dpr.hello_market.db.activity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dpr.hello_market.db.cart.CartDbModel

@Entity(tableName = "activity_table")
data class ActivityDbModel(
    @PrimaryKey val id: String,
//    @Embedded val order: List<CartDbModel>
)