package com.isp.restaurantapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.isp.restaurantapp.coroutines.Coroutines
import com.isp.restaurantapp.models.MenuItem
import com.isp.restaurantapp.models.OrderByTableId
import com.isp.restaurantapp.repositories.DataMock
import com.isp.restaurantapp.repositories.RepositoryAbstract
import kotlinx.coroutines.Job

class PayVM : ViewModel() {

    private lateinit var job: Job
    private val data: RepositoryAbstract = DataMock()

    private val _unpaiedItems = MutableLiveData<List<OrderByTableId>>()
    val unpaiedItems: LiveData<List<OrderByTableId>> = _unpaiedItems

    fun getUnpaiedItems() {

        job = Coroutines.ioTheMain(
            { data.getUnpaidOrdersByTableId(10) },
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