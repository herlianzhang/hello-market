package com.dpr.hello_market.ui.edit_profile

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import javax.inject.Inject

class EditProfileViewModel @Inject constructor(app: Application) : AndroidViewModel(app) {
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

    fun getAvatar(avatar: String) {
        val mRef =
            storageRef.getReferenceFromUrl("gs://hello-market-dpr.appspot.com")
                .child(avatar)
        mRef.downloadUrl.addOnCompleteListener {
            _photoUrl.postValue(if (it.isSuccessful) it.result else Uri.parse(""))
        }
    }
}