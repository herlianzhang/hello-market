package com.dpr.hello_market.repository

import com.dpr.hello_market.db.cart.CartDao
import com.dpr.hello_market.db.cart.CartDbModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CartRepository @Inject constructor(private val dao: CartDao) {
    val cartList = dao.getCart()

    suspend fun addCart(cart: CartDbModel) {
        withContext(Dispatchers.IO) {
            dao.insertCart(cart)
        }
    }
}