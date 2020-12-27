package com.dpr.hello_market.ui.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dpr.hello_market.helper.Helper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

class LoginViewModel @Inject constructor(app: Application) : AndroidViewModel(app) {
    private var auth: FirebaseAuth = Firebase.auth

    private val _errorEmail = MutableLiveData<String?>()
    val errorEmail: LiveData<String?>
        get() = _errorEmail

    private val _errorPassword = MutableLiveData<String?>()
    val errorPassword: LiveData<String?>
        get() = _errorPassword

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _loginSuccess = MutableLiveData<Boolean>()
    val loginSuccess: LiveData<Boolean>
        get() = _loginSuccess

    private val _loginFail = MutableLiveData<Exception>()
    val loginFail: LiveData<Exception>
        get() = _loginFail

    fun signInWithEmailAndPassword(email: String, password: String) {
        val isValidEmail = Helper.isValidEmail(email)
        val isValidPassword = Helper.isValidPassword(password)

        _errorEmail.postValue(
            if (isValidEmail) null
            else "Email not valid"
        )

        _errorPassword.postValue(
            if (isValidPassword) null
            else "Password must be at least 4 character"
        )

        if (!isValidEmail || !isValidPassword) return

        _isLoading.postValue(true)

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful)
                    _loginSuccess.postValue(true)
                else
                    _loginFail.postValue(task.exception)

                _isLoading.postValue(false)
            }
    }
}