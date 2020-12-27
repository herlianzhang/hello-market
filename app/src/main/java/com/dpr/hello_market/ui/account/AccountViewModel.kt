package com.dpr.hello_market.ui.account

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

class AccountViewModel @Inject constructor(app: Application) : AndroidViewModel(app) {
    private var auth: FirebaseAuth = Firebase.auth

    fun signOut() {
        auth.signOut()
    }
}