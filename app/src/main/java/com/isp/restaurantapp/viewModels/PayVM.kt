package com.isp.restaurantapp.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isp.restaurantapp.coroutines.Coroutines
import com.isp.restaurantapp.models.dto.OrderByTableIdDTO
import com.isp.restaurantapp.repositories.RepositoryAbstract
import com.isp.restaurantapp.repositories.RepositoryRetrofit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

    // TODO: Předělat na klasickou coroutine
    // TODO: Napojit na reálné číslo stolu!
    fun fetchUnpaidItems(tableNumber: Int) {
        if (tableNumber < 0 || tableNumber>1000){
            Log.e(TAG, "Invalid table number: $tableNumber")
            return
        }

//        job = Coroutines.ioTheMain(
//            { data.getUnpaidOrdersByTableId(tableNumber) },
//            { _unpaidItems.value = it }
//        )
        try {
            viewModelScope.launch(Dispatchers.IO){
                val result = data.getUnpaidOrdersByTableId(tableNumber)
                withContext(Dispatchers.Main){
                    _unpaidItems.postValue(result)
                }
            }
        } catch (e: Exception){
                throw e
        }
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

}