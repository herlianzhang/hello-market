package com.dpr.hello_market.ui.register

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dpr.hello_market.helper.Helper
import com.dpr.hello_market.vo.Customer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import javax.inject.Inject

class RegisterViewModel @Inject constructor(app: Application) : AndroidViewModel(app) {
    private val auth: FirebaseAuth = Firebase.auth
    private val database = Firebase.database.reference
    private val storageRef = Firebase.storage.reference

    private var imageUri: Uri? = null

    private val _errorEmail = MutableLiveData<String?>()
    val errorEmail: LiveData<String?>
        get() = _errorEmail

    private val _errorPassword = MutableLiveData<String?>()
    val errorPassword: LiveData<String?>
        get() = _errorPassword

    private val _errorName = MutableLiveData<String?>()
    val errorName: LiveData<String?>
        get() = _errorName

    private val _errorPhoneNumber = MutableLiveData<String?>()
    val errorPhoneNumber: LiveData<String?>
        get() = _errorPhoneNumber

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _registerSuccess = MutableLiveData<Boolean>()
    val registerSuccess: LiveData<Boolean>
        get() = _registerSuccess

    private val _registerFail = MutableLiveData<Exception>()
    val registerFail: LiveData<Exception>
        get() = _registerFail

    fun createUserWithEmailAndPassword(
        email: String,
        name: String,
        password: String,
        phoneNumber: String,
    ) {
        val isValidEmail = Helper.isValidEmail(email)
        val isValidPassword = Helper.isValidPassword(password)
        val isValidName = name.isNotEmpty()
        val isValidPhoneNumber = Helper.isValidPhoneNumber(phoneNumber)

        _errorEmail.postValue(
            if (isValidEmail) null
            else "Email not valid"
        )

        _errorPassword.postValue(
            if (isValidPassword) null
            else "Password must be at least 4 character"
        )

        _errorPhoneNumber.postValue(
            if (isValidPhoneNumber) null
            else "Phone Number not valid"
        )

        _errorName.postValue(
            if (isValidName) null
            else "Name cannot be empty"
        )

        if (!isValidEmail || !isValidPassword || !isValidName || !isValidPhoneNumber) return

        _isLoading.postValue(true)

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful)
                    updateProfile(name, email, password, phoneNumber)
                else {
                    _registerFail.postValue(task.exception)
                    _isLoading.postValue(false)
                }
            }
    }

    private fun updateProfile(name: String, email: String, password: String, phoneNumber: String) {
        val customer = Customer(
            name,
            email,
            "avatars/${auth.currentUser?.uid.toString()}",
            phoneNumber,
            password
        )
        database.child("customers").child(auth.currentUser?.uid.toString()).setValue(customer)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    uploadAvatar()
                } else {
                    _registerFail.postValue(task.exception)
                }
            }
    }

    private fun uploadAvatar() {
        if (imageUri == null) {
            _registerSuccess.postValue(true)
            return
        }
        imageUri?.let {
            val avatarRef = storageRef.child("avatars/${auth.currentUser?.uid.toString()}")
            avatarRef.putFile(it)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _registerSuccess.postValue(true)
                    } else {
                        _registerFail.postValue(task.exception)
                    }
                }
        }
    }

    fun setImageUri(uri: Uri?) {
        imageUri = uri
    }
}