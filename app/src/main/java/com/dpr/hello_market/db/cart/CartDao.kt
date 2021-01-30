package com.dpr.hello_market.db.cart

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CartDao {
    @Query("SELECT * FROM cart_table ORDER BY id DESC")
    fun getCart(): LiveData<List<CartDbModel>>

    @Query("DELETE FROM cart_table")
    suspend fun removeAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCart(cart: CartDbModel)

    @Delete
    suspend fun deleteCart(cart: CartDbModel)

    @Query("Select * FROM cart_table WHERE name = :name")
    suspend fun filter(name: String): List<CartDbModel>
}