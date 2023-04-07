package com.isp.restaurantapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser

class LoginVM : ViewModel() {
    


    private val _text = MutableLiveData<String>().apply {
        value = "This is log in Fragment"
    }
    val text: LiveData<String> = _text
}