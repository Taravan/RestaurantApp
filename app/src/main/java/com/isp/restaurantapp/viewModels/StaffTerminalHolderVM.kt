package com.isp.restaurantapp.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isp.restaurantapp.models.dto.FrbOrderDTO
import com.isp.restaurantapp.models.dto.OrderByTableIdDTO
import com.isp.restaurantapp.models.dto.TableDTO
import com.isp.restaurantapp.repositories.RepositoryAbstract
import com.isp.restaurantapp.repositories.RepositoryDataMock
import com.isp.restaurantapp.repositories.RepositoryRetrofit
import com.isp.restaurantapp.repositories.interfaces.TableGetterService
import kotlinx.coroutines.launch

class StaffTerminalHolderVM: ViewModel() {

    companion object {
        const val TAG = "StaffTerminalHolderVM"
    }

//    private val _repository: RepositoryAbstract by lazy {
//        RepositoryDataMock()
//        RepositoryRetrofit()
//    }

    private val _repository = RepositoryDataMock()

    private val _pendingOrders = MutableLiveData<List<FrbOrderDTO>>()
    val pendingOrders: LiveData<List<FrbOrderDTO>>
        get() = _pendingOrders

    private val _processedOrders = MutableLiveData<List<FrbOrderDTO>>()
    val processedOrders: LiveData<List<FrbOrderDTO>>
        get() = _processedOrders

    fun processPendingOrder(idOfOrder: Int) {
        val newOrder = _pendingOrders.value?.filter { it.orderId == idOfOrder } ?: emptyList()
        val currentList = _processedOrders.value ?: emptyList()
        _processedOrders.value = currentList + newOrder
        _pendingOrders.value = _pendingOrders.value?.filter { it.orderId != idOfOrder }
    }

    fun processProcessedOrder(idOfOrder: Int) {
        _processedOrders.value = _processedOrders.value?.filter { it.orderId != idOfOrder }
    }

    fun fetchPendingOrders() {
        viewModelScope.launch {
            try {
                _pendingOrders.value = _repository.getPendingOrders()
            } catch (e: Exception){
                Log.e(TAG, e.message.toString())
                throw e
            }
        }
    }

    fun fetchProcessedOrders() {
        viewModelScope.launch {
            try {
                _processedOrders.value = _repository.getProcessedOrders()
            } catch (e: Exception){
                Log.e(TAG, e.message.toString())
                throw e
            }
        }
    }

}