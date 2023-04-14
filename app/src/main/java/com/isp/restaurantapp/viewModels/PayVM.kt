package com.isp.restaurantapp.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.isp.restaurantapp.coroutines.Coroutines
import com.isp.restaurantapp.models.dto.OrderByTableIdDTO
import com.isp.restaurantapp.repositories.RepositoryAbstract
import com.isp.restaurantapp.repositories.RepositoryRetrofit
import kotlinx.coroutines.Job

class PayVM : ViewModel() {

    companion object{
        const val TAG = "PayVM"
    }


    private lateinit var job: Job
    private val data: RepositoryAbstract = RepositoryRetrofit()
    //private val data: RepositoryAbstract = RepositoryDataMock()

    private val _unpaidItems = MutableLiveData<List<OrderByTableIdDTO>>()
    val unpaidItems: LiveData<List<OrderByTableIdDTO>> = _unpaidItems

    private var selectedItemsToPay = mutableListOf<OrderByTableIdDTO>()

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

    fun updateSelectedList(operation: Boolean, item: OrderByTableIdDTO) {
        if (operation) selectedItemsToPay.add(item)
        else selectedItemsToPay.remove(item)
    }

    fun payForSelectedItems() {

        selectedItemsToPay.toList().forEach { item ->
            Log.w(TAG, item.name)
        }

    }

    init {
        getUnpaidItems()
    }

}