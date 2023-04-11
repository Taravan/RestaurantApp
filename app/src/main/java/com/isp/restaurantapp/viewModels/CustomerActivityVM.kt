package com.isp.restaurantapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.isp.restaurantapp.models.Table
import com.isp.restaurantapp.repositories.LocalRepo

class CustomerActivityVM: ViewModel() {

    private val localRepo = LocalRepo.getInstance()

    private val _table = MutableLiveData<Table>()
    val table: LiveData<Table>
        get() = _table

    private val _tableNumber = MutableLiveData<Int>()
    val tableNumber: LiveData<Int>
        get() = _tableNumber

    fun setTableNumber(number: Int) {
        _tableNumber.value = number
    }

    fun setTable(table: Table) {
        localRepo.setTable(table)
    }

    init {

        // Just for testing purposes
        _tableNumber.value = 10
        setTable(Table(0, 5, "kodStoluCisloPet"))
    }

}