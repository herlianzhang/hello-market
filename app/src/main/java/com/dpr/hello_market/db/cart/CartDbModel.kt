package com.dpr.hello_market.db.cart

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_table")
data class CartDbModel(
    @PrimaryKey val productId: String,
    val name: String,
    val price: Long,
    val total: Int,
    val unit: String
)