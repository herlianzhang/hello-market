package com.dpr.hello_market.ui.product.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dpr.hello_market.db.cart.CartDbModel
import com.dpr.hello_market.repository.CartRepository
import com.dpr.hello_market.vo.Product
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class ProductListViewModel @Inject constructor(
    app: Application,
    private val cartRepository: CartRepository
) : AndroidViewModel(app) {
    private val database = Firebase.database.reference

    private val _product = MutableLiveData<List<Product>>()
    val product: LiveData<List<Product>>
        get() = _product

    fun getProduct(category: String, subcategory: String) {
        viewModelScope.launch(Dispatchers.IO) {
            database.child("Test").setValue("d")
        }
        database.child("Category").child(category).child(subcategory).child("Subcategory")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    try {
                        viewModelScope.launch(Dispatchers.IO) {
                            val products = mutableListOf<Product>()
                            for (valueRes in snapshot.children) {
                                val data = valueRes.value as Map<String, Any?>
                                val product = Product(
                                    valueRes.key,
                                    data["Price"] as Long?,
                                    data["Quantity"] as Long?,
                                    data["Unit"] as String?,
                                    data["Picture"] as String?,
                                )
                                products.add(product)
                            }
                            _product.postValue(products)
                            Timber.d("Masuk pak eko $products")
                        }
                    } catch (e: Exception) {
                        Timber.e("get product error cause $e")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Timber.e("get product fail cause $error")
                }
            })
    }

    fun addItemToCart(product: Product, total: Int) {
        viewModelScope.launch {
            val cart = CartDbModel(
                System.currentTimeMillis().toString(),
                product.name.toString(),
                product.price ?: 0,
                total,
                product.unit.toString()
            )
            cartRepository.addCart(cart)
        }
    }
}