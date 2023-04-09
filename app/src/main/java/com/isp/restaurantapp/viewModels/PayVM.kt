package com.isp.restaurantapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.isp.restaurantapp.coroutines.Coroutines
import com.isp.restaurantapp.models.MenuItem
import com.isp.restaurantapp.repositories.DataMock
import kotlinx.coroutines.Job

class PayVM : ViewModel() {

    private lateinit var job: Job
    private val data: DataMock = DataMock()

    private val _unpaiedItems = MutableLiveData<List<MenuItem>>()
    val unpaiedItems: LiveData<List<MenuItem>> = _unpaiedItems

    fun getUnpaiedItems() {

        job = Coroutines.ioTheMain(
            { data.getUnpaidOrdersByTableId() },
            { _unpaiedItems.value = it }
        )

    }

    override fun onCleared() {
        super.onCleared()
        if (::job.isInitialized) job.cancel()
    }

    init {
        getUnpaiedItems()
    }

}