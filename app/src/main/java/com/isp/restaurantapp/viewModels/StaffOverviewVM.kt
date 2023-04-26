package com.isp.restaurantapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.isp.restaurantapp.models.dto.FrbOrderDTO

class StaffOverviewVM: ViewModel() {

    private val _allOrders = MutableLiveData<List<FrbOrderDTO>>()
    val allOrders: LiveData<List<FrbOrderDTO>>
        get() = _allOrders

    private val _paidOrders = MutableLiveData<List<FrbOrderDTO>>()
    val paidOrders: LiveData<List<FrbOrderDTO>>
        get() = _paidOrders

    init {
        _allOrders.value = listOf(
            FrbOrderDTO("0", 0, 0, "ObjJedna", Timestamp.now(), 0.0, 0, "Paid", 0, 0, "0"),
            FrbOrderDTO("1", 1, 1, "ObjDva", Timestamp.now(), 0.1, 1, "Pending", 1, 1, "1")
        )

        _paidOrders.value = listOf(
            FrbOrderDTO("0", 0, 0, "ObjJedna", Timestamp.now(), 0.0, 0, "Paid", 0, 0, "0")
        )

    }

}