package com.dpr.hello_market.ui.edit_profile

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dpr.hello_market.helper.Helper
import com.dpr.hello_market.vo.Customer
import com.dpr.hello_market.vo.Location
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import javax.inject.Inject

class EditProfileViewModel @Inject constructor(app: Application) : AndroidViewModel(app) {
    private val auth: FirebaseAuth = Firebase.auth
    private val database = Firebase.database.reference
    private val storageRef = Firebase.storage

    var imageUri: Uri? = null

    private val _errorEmail = MutableLiveData<String?>()
    val errorEmail: LiveData<String?>
        get() = _errorEmail

    private val _errorName = MutableLiveData<String?>()
    val errorName: LiveData<String?>
        get() = _errorName

    private val _errorPhoneNumber = MutableLiveData<String?>()
    val errorPhoneNumber: LiveData<String?>
        get() = _errorPhoneNumber

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _photoUrl = MutableLiveData<Uri>()
    val photoUrl: LiveData<Uri>
        get() = _photoUrl

    private val _location = MutableLiveData<Location>()
    val location: LiveData<Location>
        get() = _location

    private val _editSuccess = MutableLiveData<Boolean>()
    val editSuccess: LiveData<Boolean>
        get() = _editSuccess

    private val _editFail = MutableLiveData<Exception>()
    val editFail: LiveData<Exception>
        get() = _editFail

    fun setLocation(location: Location?) {
        location?.let {
            _location.postValue(it)
        }
    }

    fun getAvatar(avatar: String) {
        val mRef =
            storageRef.getReferenceFromUrl("gs://hello-market-dpr.appspot.com")
                .child(avatar)
        mRef.downloadUrl.addOnCompleteListener {
            _photoUrl.postValue(if (it.isSuccessful) it.result else Uri.parse(""))
        }
    }

    fun editProfile(customer: Customer) {
        val isValidName = customer.name?.isNotEmpty() ?: false
        val isValidPhoneNumber = Helper.isValidPhoneNumber(customer.phoneNumber ?: "")

        _errorPhoneNumber.postValue(
            if (isValidPhoneNumber) null
            else "Phone Number not valid"
        )

        _errorName.postValue(
            if (isValidName) null
            else "Name cannot be empty"
        )

        if (!isValidName || !isValidPhoneNumber) return

        database.child("customers").child(auth.currentUser?.uid.toString()).setValue(customer)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _editSuccess.postValue(true)
                } else {
                    _editFail.postValue(task.exception)
                }
            }
    }
}