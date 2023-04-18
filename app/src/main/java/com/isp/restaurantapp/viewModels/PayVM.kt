package com.isp.restaurantapp.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isp.restaurantapp.models.Resource
import com.isp.restaurantapp.models.dto.FrbOrderDTO
import com.isp.restaurantapp.models.firebase.FrbFieldsOrders
import com.isp.restaurantapp.repositories.concrete.FrbOrderStateUpdater
import com.isp.restaurantapp.repositories.concrete.FrbRealtimeOrder
import com.isp.restaurantapp.repositories.interfaces.FrbDocumentStateUpdaterService
import com.isp.restaurantapp.repositories.interfaces.FrbRealtimeGetterByTableIdService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PayVM : ViewModel() {

    companion object{
        const val TAG = "PayVM"
    }


    private lateinit var job: Job

    /**
     * This lines are no longer needed since this activity works only via Firestore
     */
    //private val data: RepositoryAbstract = RepositoryRetrofit()
    //private val data: RepositoryAbstract = RepositoryDataMock()

    private val _orderUpdater: FrbDocumentStateUpdaterService<FrbOrderDTO> by lazy{
        FrbOrderStateUpdater()
    }


//    private val _unpaidItems = MutableLiveData<List<OrderByTableIdDTO>>()
//    val unpaidItems: LiveData<List<OrderByTableIdDTO>> = _unpaidItems


    /*
    **** STARÉ S UNPAID ITEMS
    private var _selectedItemsToPay = mutableListOf<OrderByTableIdDTO>()
    val selectedItemsToPay: List<OrderByTableIdDTO>
        get() = _selectedItemsToPay
     */

    private var _selectedItemsToPay = mutableListOf<FrbOrderDTO>()
    val selectedItemsToPay: List<FrbOrderDTO>
        get() = _selectedItemsToPay

//    fun fetchUnpaidItems(tableNumber: Int) {
//        if (tableNumber < 0 || tableNumber>1000){
//            Log.e(TAG, "Invalid table number: $tableNumber")
//            return
//        }
//        try {
//            viewModelScope.launch(Dispatchers.IO){
//                val result = data.getUnpaidOrdersByTableId(tableNumber)
//                withContext(Dispatchers.Main){
//                    _unpaidItems.postValue(result)
//                }
//            }
//        } catch (e: Exception){
//                throw e
//        }
//    }

    /**
     * REALTIME
     */
    private val _ordersRealtimeRepository: FrbRealtimeGetterByTableIdService<FrbOrderDTO> by lazy{
        FrbRealtimeOrder()
    }
    fun getRealtimeOrders(tableId: Int): LiveData<List<FrbOrderDTO>>{
        val itemsMLD = MutableLiveData<List<FrbOrderDTO>>()

        viewModelScope.launch(Dispatchers.IO){
            _ordersRealtimeRepository.getItemsRealtime(tableId).collect() {
                withContext(Dispatchers.Main){
                    itemsMLD.value = it.sortedBy { it.state }
                }
            }
        }
        return itemsMLD
    }

    override fun onCleared() {
        super.onCleared()
        if (::job.isInitialized) job.cancel()
    }

    fun updateSelectedList(operation: Boolean, item: FrbOrderDTO) {
        if (operation) _selectedItemsToPay.add(item)
        else _selectedItemsToPay.remove(item)
    }

    fun payForSelectedItems(uid: String) {
        Log.i(TAG, "Payment request in viewmodel with uid= $uid")
        viewModelScope.launch(Dispatchers.IO){
            val newState = FrbFieldsOrders.States.FOR_PAYMENT
            val result = _orderUpdater.updateDocuments(_selectedItemsToPay, newState, uid)
            withContext(Dispatchers.Main){
                when(result){
                    is Resource.Failure -> throw result.exception
                    is Resource.Success -> _selectedItemsToPay.clear()
                    else -> {}
                }
            }
        }

    }
//
//    fun updateSelectedList(operation: Boolean, item: OrderByTableIdDTO) {
//        if (operation) _selectedItemsToPay.add(item)
//        else _selectedItemsToPay.remove(item)
//    }
//
//    fun payForSelectedItems(uid: String) {
//        val documents = mutableListOf<FrbOrderDTO>()
//
//        _selectedItemsToPay.toList().forEach { item ->
//            documents.add(FrbOrderMapper.toFrbOrderDTO(item, FrbFieldsOrders.States.FOR_PAYMENT, uid))
//            Log.w(TAG, item.goodsName)
//        }
//        viewModelScope.launch(Dispatchers.IO){
//            val result = _orderInserter.insertDocuments(documents, uid)
//            withContext(Dispatchers.Main){
//                when(result){
//                    is Resource.Failure -> { throw result.exception }
//                    else -> {}
//                }
//            }
//        }
//
//    }

}