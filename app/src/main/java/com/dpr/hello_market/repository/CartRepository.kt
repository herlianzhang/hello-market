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
            val getCart = dao.filter(cart.name)
            if (getCart.isEmpty()) dao.insertCart(cart)
            else {
                val curr = getCart.first()
                dao.insertCart(curr.copy(total = curr.total + cart.total))
            }
        }
    }

    suspend fun replaceCart(cart: CartDbModel) {
        withContext(Dispatchers.IO) {
            dao.insertCart(cart)
        }
    }

    suspend fun removeCart(cart: CartDbModel) {
        withContext(Dispatchers.IO) {
            dao.deleteCart(cart)
        }
    }

    suspend fun removeAll() {
        withContext(Dispatchers.IO) {
            dao.removeAll()
        }
    }
}