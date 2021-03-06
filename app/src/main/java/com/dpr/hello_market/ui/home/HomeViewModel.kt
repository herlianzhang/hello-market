package com.dpr.hello_market.ui.home

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dpr.hello_market.BuildConfig
import com.dpr.hello_market.vo.Banner
import com.dpr.hello_market.vo.Category
import com.dpr.hello_market.vo.Customer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class HomeViewModel @Inject constructor(app: Application) : AndroidViewModel(app) {
    private val auth: FirebaseAuth = Firebase.auth
    private val database = Firebase.database.reference
    private val storageRef = Firebase.storage

    var customer: Customer? = null

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _name = MutableLiveData<String>()
    val name: LiveData<String>
        get() = _name

    private val _greeting = MutableLiveData<String>()
    val greeting: LiveData<String>
        get() = _greeting

    private val _photoUrl = MutableLiveData<Uri>()
    val photoUrl: LiveData<Uri>
        get() = _photoUrl

    private val _categories = MutableLiveData<List<Category>>()
    val categories: LiveData<List<Category>>
        get() = _categories

    private val _banner = MutableLiveData<List<Banner>>()
    val banner: LiveData<List<Banner>>
        get() = _banner

    init {
        _isLoading.postValue(true)

        database.child("customers").child(auth.currentUser?.uid.toString())
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val data = snapshot.getValue<Customer>()
                    customer = data
                    data?.name?.let {
                        _name.postValue("Hello $it")
                    }
                    _greeting.postValue("What are you looking for?")
                    auth.currentUser?.email
                    updateAvatar()
                    _isLoading.postValue(false)
                }

                override fun onCancelled(error: DatabaseError) {
                    Timber.e("get customer fail cause $error")
                }
            })

        database.child("Category").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    val categories = mutableListOf<Category>()
                    for (valueRes in snapshot.children) {
                        categories.add(
                            Category(
                                valueRes.key,
                                (valueRes.value as Map<String, Any?>)["Picture"] as String?
                            )
                        )
                    }
                    Timber.d("listnya $categories")
                    _categories.postValue(categories)
                } catch (e: Exception) {
                    Timber.e("Get Category error ${e.message}")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Timber.e("get category fail cause $error")
            }
        })

        database.child("Banner").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    val banners = mutableListOf<Banner>()
                    for (valueRes in snapshot.children) {
                        banners.add(Banner(valueRes.value as String?))
                    }
                    Timber.d("bannernya $banners")
                    _banner.postValue(banners)
                } catch (e: Exception) {
                    Timber.e("Get Banner error ${e.message}")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Timber.e("get Banner fail cause $error")
            }

        })
    }

    fun updateAvatar() {
        viewModelScope.launch(Dispatchers.IO) {
            val mRef =
                storageRef.getReferenceFromUrl(BuildConfig.STORAGE_URL)
                    .child(customer?.avatar ?: "")
            mRef.downloadUrl.addOnCompleteListener {
                _photoUrl.postValue(if (it.isSuccessful) it.result else Uri.parse(""))
            }
        }
    }
}