package com.isp.restaurantapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.isp.restaurantapp.coroutines.Coroutines
import com.isp.restaurantapp.models.Item
import com.isp.restaurantapp.repositories.DataMock
import kotlinx.coroutines.Job

class PayVM : ViewModel() {

    private lateinit var job: Job
    private val data: DataMock = DataMock()

    private val _unpaiedItems = MutableLiveData<List<Item>>()
    val unpaiedItems: LiveData<List<Item>> = _unpaiedItems

    fun getUnpaiedItems() {

        job = Coroutines.ioTheMain(
            { data.getUnpaidItems() },
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