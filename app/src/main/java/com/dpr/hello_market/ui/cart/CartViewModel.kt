package com.dpr.hello_market.ui.cart

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dpr.hello_market.db.activity.ActivityDbModel
import com.dpr.hello_market.db.cart.CartDbModel
import com.dpr.hello_market.repository.ActivityRepository
import com.dpr.hello_market.repository.CartRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class CartViewModel @Inject constructor(
    app: Application,
    private val cartRepository: CartRepository,
    private val activityRepository: ActivityRepository
) : AndroidViewModel(app) {
    val cart = cartRepository.cartList

    fun replaceCart(cart: CartDbModel) {
        viewModelScope.launch {
            cartRepository.replaceCart(cart)
        }
    }

    fun removeCart(cart: CartDbModel) {
        viewModelScope.launch {
            cartRepository.removeCart(cart)
        }
    }

    fun removeAll() {
        viewModelScope.launch {
            cartRepository.removeAll()
        }
    }

    fun addActivity(product: List<CartDbModel>) {
        viewModelScope.launch {
            activityRepository.addActivity(ActivityDbModel(order = product))
        }
    }
}