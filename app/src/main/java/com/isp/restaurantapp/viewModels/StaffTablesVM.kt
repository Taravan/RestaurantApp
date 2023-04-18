package com.isp.restaurantapp.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isp.restaurantapp.models.dto.FrbOrderDTO
import com.isp.restaurantapp.models.dto.TableDTO
import com.isp.restaurantapp.repositories.RepositoryDataMock
import kotlinx.coroutines.launch

class StaffTablesVM: ViewModel() {

    companion object {
        const val TAG = "StaffTablesVM"
    }

//    private val _repository: RepositoryAbstract by lazy {
//        RepositoryDataMock()
//        RepositoryRetrofit()
//    }

    private val _repository = RepositoryDataMock()

    private val _tables = MutableLiveData<List<TableDTO>>()
    val tables: LiveData<List<TableDTO>>
        get() = _tables

    private val _selectedTable = MutableLiveData<TableDTO>()
    val selectedTable: LiveData<TableDTO>
        get() = _selectedTable

    private val _markedToPay = MutableLiveData<List<FrbOrderDTO>>()
    val markedToPay: LiveData<List<FrbOrderDTO>>
        get() = _markedToPay

    private val _leftToPay = MutableLiveData<List<FrbOrderDTO>>()
    val leftToPay: LiveData<List<FrbOrderDTO>>
        get() = _leftToPay

    private val _priceToPay = MutableLiveData<Double>()
    val priceToPay: LiveData<Double>
        get() = _priceToPay

    private val _priceLeftToPay = MutableLiveData<Double>()
    val priceLeftToPay: LiveData<Double>
        get() = _priceLeftToPay

    fun fetchTables() {
        viewModelScope.launch {
            try {
                _tables.value = _repository.getTables()
            } catch (e: Exception){
                Log.e(TAG, e.message.toString())
                throw e
            }
        }
    }

    fun fetchLeftToPay() {
        viewModelScope.launch {
            try {
                _leftToPay.value = _repository.getProcessedOrders()
            } catch (e: Exception){
                Log.e(TAG, e.message.toString())
                throw e
            }
        }
    }

    fun onPay() {

    }

    fun onSelectTable(tableId: Int) {

    }

    fun onMoveItemToMarkedToPay(orderId: Int) {

    }

    fun onMoveItemToLeftToPay(orderId: Int) {

    }

}