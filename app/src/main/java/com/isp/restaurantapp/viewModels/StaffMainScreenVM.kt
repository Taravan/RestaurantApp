package com.isp.restaurantapp.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.isp.restaurantapp.models.StaffAccount

class StaffMainScreenVM: ViewModel() {

    companion object {
        const val TAG = "StaffMainScreenVM"
    }

    private val _staffAccount: MutableLiveData<StaffAccount> by lazy{
        MutableLiveData<StaffAccount>()
    }
    val staffAccount: LiveData<StaffAccount>
        get() = _staffAccount




    fun setStaffAccount(staffAccount: StaffAccount){
        _staffAccount.postValue(staffAccount)
    }

    fun logOut() {
        //TODO: Finish logout
        Log.e(TAG, "Log out")
    }
}