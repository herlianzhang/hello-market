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
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
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
    ) {
        val isValidEmail = Helper.isValidEmail(email)
        val isValidPassword = Helper.isValidPassword(password)
        val isValidName = name.isNotEmpty()

        _errorEmail.postValue(
            if (isValidEmail) null
            else "Email not valid"
        )

        _errorPassword.postValue(
            if (isValidPassword) null
            else "Password must be at least 4 character"
        )

        _errorName.postValue(
            if (isValidName) null
            else "Name cannot be empty"
        )

        if (!isValidEmail || !isValidPassword || !isValidName) return

        _isLoading.postValue(true)

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful)
                    updateProfile(name)
                else {
                    _registerFail.postValue(task.exception)
                    _isLoading.postValue(false)
                }
            }
    }

    private fun updateProfile(name: String) {
        val customer = Customer(name, "avatars/${auth.currentUser?.uid.toString()}.jpg")
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
        imageUri?.let {
            val avatarRef = storageRef.child("avatars/${auth.currentUser?.uid.toString()}.jpg")
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