package com.dpr.hello_market.ui.home

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dpr.hello_market.BuildConfig
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

    private val _name = MutableLiveData<String>()
    val name: LiveData<String>
        get() = _name

    private val _photoUrl = MutableLiveData<Uri>()
    val photoUrl: LiveData<Uri>
        get() = _photoUrl

    init {
        database.child("customers").child(auth.currentUser?.uid.toString())
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val data = snapshot.getValue<Customer>()
                    customer = data
                    data?.name?.let {
                        _name.postValue("Hello $it")
                    }
                    auth.currentUser?.email
                    updateAvatar()
                }

                override fun onCancelled(error: DatabaseError) {
                    Timber.e("$error")
                }
            })
    }


    fun updateAvatar() {
        viewModelScope.launch(Dispatchers.IO) {
            val mRef =
                storageRef.getReferenceFromUrl("gs://hello-market-dpr.appspot.com")
                    .child(customer?.avatar ?: "")
            mRef.downloadUrl.addOnCompleteListener {
                _photoUrl.postValue(if (it.isSuccessful) it.result else Uri.parse(""))
            }
        }
    }
}