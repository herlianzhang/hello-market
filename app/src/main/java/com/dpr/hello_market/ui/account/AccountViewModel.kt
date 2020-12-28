package com.dpr.hello_market.ui.account

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
import timber.log.Timber
import javax.inject.Inject

class AccountViewModel @Inject constructor(app: Application) : AndroidViewModel(app) {
    private val auth: FirebaseAuth = Firebase.auth
    private val database = Firebase.database.reference
    private val storageRef = Firebase.storage.reference

    private val _name = MutableLiveData<String>()
    val name: LiveData<String>
        get() = _name

    private val _photoUrl = MutableLiveData<String>()
    val photoUrl: LiveData<String>
        get() = _photoUrl

    init {
        database.child("customers").child(auth.currentUser?.uid.toString())
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val data = snapshot.getValue<Customer>()
                    _name.postValue(data?.name)
                    _photoUrl.postValue(data?.avatar)
                }

                override fun onCancelled(error: DatabaseError) {
                    Timber.e("$error")
                }

            })
    }

    fun signOut() {
        auth.signOut()
    }
}