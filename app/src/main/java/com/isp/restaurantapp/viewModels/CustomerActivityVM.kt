package com.isp.restaurantapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.isp.restaurantapp.models.Table

class CustomerActivityVM: ViewModel() {

    private val _table = MutableLiveData<Table>()
    val table: LiveData<Table>
        get() = _table

    private val _tableNumber = MutableLiveData<Int>()
    val tableNumber: LiveData<Int>
        get() = _tableNumber

    init {
        _tableNumber.value = 10
    }

}