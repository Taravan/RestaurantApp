package com.isp.restaurantapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PayVM : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is paying Fragment"
    }
    val text: LiveData<String> = _text
}