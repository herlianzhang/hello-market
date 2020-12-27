package com.dpr.hello_market.ui.decision

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

class DecisionViewModel @Inject constructor(app: Application) : AndroidViewModel(app) {
    private var auth: FirebaseAuth = Firebase.auth

    private val _isAuthenticated = MutableLiveData<Boolean>()
    val isAuthenticated: LiveData<Boolean>
        get() = _isAuthenticated

    init {
        _isAuthenticated.postValue(auth.currentUser != null)
    }
}