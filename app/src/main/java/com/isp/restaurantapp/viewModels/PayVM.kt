package com.isp.restaurantapp.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isp.restaurantapp.models.FrbOrderMapper
import com.isp.restaurantapp.models.Resource
import com.isp.restaurantapp.models.dto.FrbOrderDTO
import com.isp.restaurantapp.models.dto.OrderByTableIdDTO
import com.isp.restaurantapp.models.firebase.FrbFieldsOrders
import com.isp.restaurantapp.repositories.RepositoryAbstract
import com.isp.restaurantapp.repositories.RepositoryRetrofit
import com.isp.restaurantapp.repositories.concrete.FrbOrdersInsertService
import com.isp.restaurantapp.repositories.interfaces.FrbDocumentsInsertService
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

    private val _orderInsterter: FrbDocumentsInsertService<FrbOrderDTO> by lazy{
        FrbOrdersInsertService()
    }

    private val _unpaidItems = MutableLiveData<List<OrderByTableIdDTO>>()
    val unpaidItems: LiveData<List<OrderByTableIdDTO>> = _unpaidItems

    private var _selectedItemsToPay = mutableListOf<OrderByTableIdDTO>()
    val selectedItemsToPay: List<OrderByTableIdDTO>
        get() = _selectedItemsToPay

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
        if (operation) _selectedItemsToPay.add(item)
        else _selectedItemsToPay.remove(item)
    }

    fun payForSelectedItems(uid: String) {
        val documents = mutableListOf<FrbOrderDTO>()

        _selectedItemsToPay.toList().forEach { item ->
            documents.add(FrbOrderMapper.toFrbOrder(item, FrbFieldsOrders.States.FOR_PAYMENT, uid))
            Log.w(TAG, item.goodsName)
        }
        viewModelScope.launch(Dispatchers.IO){
            val result = _orderInsterter.insertDocuments(documents, uid)
            withContext(Dispatchers.Main){
                when(result){
                    is Resource.Failure -> { throw result.exception }
                    else -> {}
                }
            }
        }

    }

}