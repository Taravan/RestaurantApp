package com.isp.restaurantapp.viewModels

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.isp.restaurantapp.models.Table

class AllergensVM(): ViewModel() {

    private val _table = MutableLiveData<Table>()
    val table: LiveData<Table>
        get() = _table

    fun setTable(table: Table) {
        _table.value = table
    }

    init {


    }

}