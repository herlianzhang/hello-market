package com.dpr.hello_market.ui.edit_profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import javax.inject.Inject

class EditProfileViewModel @Inject constructor(app: Application) : AndroidViewModel(app) {
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
}