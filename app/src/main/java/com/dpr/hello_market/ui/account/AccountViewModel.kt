package com.dpr.hello_market.ui.account

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import java.io.File
import javax.inject.Inject

class AccountViewModel @Inject constructor(app: Application) : AndroidViewModel(app) {
    private val auth: FirebaseAuth = Firebase.auth
    private val database = Firebase.database.reference
    private val storageRef = Firebase.storage

    private val _name = MutableLiveData<String>()
    val name: LiveData<String>
        get() = _name

    private val _email = MutableLiveData<String>()
    val email: LiveData<String>
        get() = _email

    private val _versionCode = MutableLiveData<String>()
    val versionCode: LiveData<String>
        get() = _versionCode

    private val _photoUrl = MutableLiveData<Uri>()
    val photoUrl: LiveData<Uri>
        get() = _photoUrl

    init {
        Timber.d("masuk accountviewmodel pak eko")

        database.child("customers").child(auth.currentUser?.uid.toString())
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val data = snapshot.getValue<Customer>()
                    _name.postValue(data?.name)
                    _email.postValue(data?.email)
                    _versionCode.postValue("Version ${BuildConfig.VERSION_NAME}")
                    auth.currentUser?.email
                    viewModelScope.launch(Dispatchers.IO) {
                        val mRef =
                            storageRef.getReferenceFromUrl("gs://hello-market-dpr.appspot.com")
                                .child(data?.avatar ?: "")
                        mRef.downloadUrl.addOnCompleteListener {
                            _photoUrl.postValue(it.result)
                        }
//                        val mFile = File.createTempFile("avatar", "*")
//                        mRef.getFile(mFile).addOnSuccessListener {
//                            val bitmap = BitmapFactory.decodeFile(mFile.absolutePath)
//                        }
                    }
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