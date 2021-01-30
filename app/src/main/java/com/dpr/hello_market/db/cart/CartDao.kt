package com.dpr.hello_market.db.cart

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CartDao {
    @Query("SELECT * FROM cart_table ORDER BY productId DESC")
    fun getCart(): LiveData<List<CartDbModel>>

    @Query("DELETE FROM cart_table")
    suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCart(cart: CartDbModel)
}