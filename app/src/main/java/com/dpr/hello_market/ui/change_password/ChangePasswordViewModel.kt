package com.dpr.hello_market.ui.change_password

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dpr.hello_market.helper.Helper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

class ChangePasswordViewModel @Inject constructor(app: Application) : AndroidViewModel(app) {
    private val auth: FirebaseAuth = Firebase.auth
    private val database = Firebase.database.reference

    private val _errorOldPassword = MutableLiveData<String>()
    val errorOldPassword: LiveData<String>
        get() = _errorOldPassword

    private val _errorNewPassword = MutableLiveData<String>()
    val errorNewPassword: LiveData<String>
        get() = _errorNewPassword

    private val _errorConfirmPassword = MutableLiveData<String>()
    val errorConfirmPassword: LiveData<String>
        get() = _errorConfirmPassword

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _changePasswordSuccess = MutableLiveData<Boolean>()
    val changePasswordSuccess: LiveData<Boolean>
        get() = _changePasswordSuccess

    private val _changePasswordFail = MutableLiveData<String>()
    val changePasswordFail: LiveData<String>
        get() = _changePasswordFail

    fun changePassword(
        currentPassword: String,
        oldPassword: String,
        newPassword: String,
        confirmPassword: String
    ) {
        val isValidOldPassword = Helper.isValidPassword(oldPassword)
        val isValidNewPassword = Helper.isValidPassword(newPassword)
        val isValidConfirmPassword = newPassword == confirmPassword

        _errorOldPassword.postValue(
            if (isValidOldPassword) null
            else "Password must be at least 6 character"
        )

        _errorNewPassword.postValue(
            if (isValidNewPassword) null
            else "Password must be at least 6 character"
        )

        _errorConfirmPassword.postValue(
            if (isValidConfirmPassword) null
            else "New password and Confirm password must be match"
        )

        if (!isValidConfirmPassword || !isValidNewPassword || !isValidOldPassword) return

        _isLoading.postValue(true)

        when {
            currentPassword != oldPassword -> {
                _changePasswordFail.postValue("Your old password is incorrect")
                _isLoading.postValue(false)
            }
            oldPassword == newPassword -> {
                _changePasswordFail.postValue("Your new password cannot be same as old password")
                _isLoading.postValue(false)
            }
            else -> {
                val user = auth.currentUser
                user?.updatePassword(newPassword)?.addOnCompleteListener {
                    if (it.isSuccessful) {
                        database.child("customers").child(auth.currentUser?.uid.toString())
                            .child("password").setValue(newPassword)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    _changePasswordSuccess.postValue(true)
                                } else {
                                    _changePasswordFail.postValue(it.exception?.message)
                                }
                                _isLoading.postValue(false)
                            }
                    } else {
                        _changePasswordFail.postValue(it.exception?.message)
                        _isLoading.postValue(false)
                    }
                }
            }
        }
    }
}