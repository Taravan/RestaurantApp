package com.isp.restaurantapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.isp.restaurantapp.coroutines.Coroutines
import com.isp.restaurantapp.models.OrderByTableId
import com.isp.restaurantapp.repositories.RepositoryAbstract
import com.isp.restaurantapp.repositories.RepositoryRetrofit
import kotlinx.coroutines.Job

class PayVM : ViewModel() {

    private lateinit var job: Job
    private val data: RepositoryAbstract = RepositoryRetrofit()
    //private val data: RepositoryAbstract = RepositoryDataMock()

    private val _unpaidItems = MutableLiveData<List<OrderByTableId>>()
    val unpaidItems: LiveData<List<OrderByTableId>> = _unpaidItems

    private fun getUnpaidItems() {

        job = Coroutines.ioTheMain(
            { data.getUnpaidOrdersByTableId(1) },
            { _unpaidItems.value = it }
        )

    }

    override fun onCleared() {
        super.onCleared()
        if (::job.isInitialized) job.cancel()
    }

    init {
        getUnpaidItems()
    }

}