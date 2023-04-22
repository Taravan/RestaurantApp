package com.isp.restaurantapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.isp.restaurantapp.models.StaffAccount

class StaffMainScreenVM: ViewModel() {

    private val _staffAccount: MutableLiveData<StaffAccount> by lazy{
        MutableLiveData<StaffAccount>()
    }
    val staffAccount: LiveData<StaffAccount>
        get() = _staffAccount




    fun setStaffAccount(staffAccount: StaffAccount){
        _staffAccount.postValue(staffAccount)
    }
}