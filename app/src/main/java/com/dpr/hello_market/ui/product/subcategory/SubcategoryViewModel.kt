package com.dpr.hello_market.ui.product.subcategory

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dpr.hello_market.vo.Category
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import timber.log.Timber
import javax.inject.Inject

class SubcategoryViewModel @Inject constructor(app: Application) : AndroidViewModel(app) {
    private val database = Firebase.database.reference

    private val _id = MutableLiveData<String>()
    val id: LiveData<String>
        get() = _id

    private val _subcategories = MutableLiveData<List<Category>>()
    val subcategories: LiveData<List<Category>>
        get() = _subcategories

    fun getSubCategory(id: String) {
        _id.postValue(id)
        database.child("Category").child(id).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    val subcategory = mutableListOf<Category>()
                    Timber.d("snapshot $snapshot")
                    for (valueRes in snapshot.children) {
                        if (valueRes.key != "Picture") {
                            subcategory.add(
                                Category(
                                    valueRes.key,
                                    (valueRes.value as Map<String, Any?>)["Picture"] as String?
                                )
                            )
                        }
                    }
                    Timber.d("subcategory: $subcategory")
                    _subcategories.postValue(subcategory)
                } catch (e: Exception) {
                    Timber.e("get subcategory error ${e.message}")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Timber.e("get subcategory fail cause $error")
            }

        })
    }
}