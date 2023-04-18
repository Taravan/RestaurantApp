package com.isp.restaurantapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isp.restaurantapp.models.dto.FrbOrderDTO
import com.isp.restaurantapp.models.firebase.FrbFieldsOrders
import com.isp.restaurantapp.repositories.RepositoryDataMock
import com.isp.restaurantapp.repositories.concrete.FrbRealtimeOrderPending
import com.isp.restaurantapp.repositories.interfaces.FrbRealtimeByStateGetterService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StaffTerminalHolderVM: ViewModel() {

    companion object {
        const val TAG = "StaffTerminalHolderVM"
    }

//    private val _repository: RepositoryAbstract by lazy {
//        RepositoryDataMock()
//        RepositoryRetrofit()
//    }

    private val _repository = RepositoryDataMock()

    private val _ordersRealtimeRepository: FrbRealtimeByStateGetterService<FrbOrderDTO> by lazy {
        FrbRealtimeOrderPending()
    }
    fun getPendingOrders(): LiveData<List<FrbOrderDTO>>{
        val itemsMLD = MutableLiveData<List<FrbOrderDTO>>()

        viewModelScope.launch(Dispatchers.IO){
            _ordersRealtimeRepository.getItemsRealtime(
                FrbFieldsOrders.States.PENDING)
                .collect() {
                withContext(Dispatchers.Main){
                    itemsMLD.value = it.sortedBy { it.lastUpdate }
                }
            }
        }
        return itemsMLD
    }
    fun getConfirmedOrders(): LiveData<List<FrbOrderDTO>>{
        val itemsMLD = MutableLiveData<List<FrbOrderDTO>>()

        viewModelScope.launch(Dispatchers.IO){
            _ordersRealtimeRepository.getItemsRealtime(
                FrbFieldsOrders.States.CONFIRMED)
                .collect() {
                withContext(Dispatchers.Main){
                    itemsMLD.value = it.sortedBy { it.lastUpdate }
                }
            }
        }
        return itemsMLD
    }

    private val _processedOrders = MutableLiveData<List<FrbOrderDTO>>()
    val processedOrders: LiveData<List<FrbOrderDTO>>
        get() = _processedOrders

//    fun processPendingOrder(idOfOrder: Int) {
//        val newOrder = _pendingOrders.value?.filter { it.orderId == idOfOrder } ?: emptyList()
//        val currentList = _processedOrders.value ?: emptyList()
//        _processedOrders.value = currentList + newOrder
//        _pendingOrders.value = _pendingOrders.value?.filter { it.orderId != idOfOrder }
//    }
//
//    fun processProcessedOrder(idOfOrder: Int) {
//        _processedOrders.value = _processedOrders.value?.filter { it.orderId != idOfOrder }
//    }
//
//    fun fetchPendingOrders() {
//        viewModelScope.launch {
//            try {
//                _pendingOrders.value = _repository.getPendingOrders()
//            } catch (e: Exception){
//                Log.e(TAG, e.message.toString())
//                throw e
//            }
//        }
//    }
//
//    fun fetchProcessedOrders() {
//        viewModelScope.launch {
//            try {
//                _processedOrders.value = _repository.getProcessedOrders()
//            } catch (e: Exception){
//                Log.e(TAG, e.message.toString())
//                throw e
//            }
//        }
//    }

}