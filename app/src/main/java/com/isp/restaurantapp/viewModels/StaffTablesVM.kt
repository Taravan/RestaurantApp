package com.isp.restaurantapp.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isp.restaurantapp.models.dto.FrbOrderDTO
import com.isp.restaurantapp.models.dto.TableDTO
import com.isp.restaurantapp.models.firebase.FrbFieldsOrders
import com.isp.restaurantapp.repositories.RepositoryAbstract
import com.isp.restaurantapp.repositories.RepositoryRetrofit
import com.isp.restaurantapp.repositories.concrete.FrbRealtimeGetterByTableIdAndStateServiceImpl
import com.isp.restaurantapp.repositories.interfaces.FrbRealtimeGetterByTableIdAndStateService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StaffTablesVM: ViewModel() {

    companion object {
        const val TAG = "StaffTablesVM"
    }

    private val _ordersRealtimeRepository: FrbRealtimeGetterByTableIdAndStateService<FrbOrderDTO>
    by lazy {
        FrbRealtimeGetterByTableIdAndStateServiceImpl()
    }

    /**
     * Repository for database actions, eg. Tables getter
     */
    private val _repository: RepositoryAbstract by lazy {
        //RepositoryDataMock()
        RepositoryRetrofit()
    }

    private val _errorState: MutableLiveData<String> by lazy{
        MutableLiveData<String>()
    }
    val errorState: LiveData<String>
        get() = _errorState

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
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val tables = _repository.getTables()
                withContext(Dispatchers.Main){
                    _tables.postValue(tables)
                }
            } catch (e: Exception){
                withContext(Dispatchers.Main){
                    Log.e(TAG, e.message.toString())
                    _errorState.postValue("Error occured while fetching tables from database")
                }
            }
        }
    }


    fun resetErrorState(){
        _errorState.postValue("")
    }
//    fun fetchLeftToPay(tableId: Int) {
//        viewModelScope.launch {
//            try {
//                _leftToPay.value = _repository.getProcessedOrders().filter { it.tableId == tableId }
//                _priceLeftToPay.value = _leftToPay.value?.sumOf { it.price }
//            } catch (e: Exception){
//                Log.e(TAG, e.message.toString())
//                throw e
//            }
//        }
//    }

    fun onPay() {
        _markedToPay.value = emptyList()
        _priceToPay.value = 0.0
    }

//    fun onSelectTable(tableId: Int) {
//        _selectedTable.value = _tables.value?.find { it.id == tableId }
//        fetchLeftToPay(tableId)
//    }

    fun onMoveItemToMarkedToPay(orderId: Int) {

    }

//    fun onMoveItemToLeftToPay(orderId: Int) {
//        val newItem = _markedToPay.value?.filter { it.orderId == orderId } ?: emptyList()
//        val currentList = _leftToPay.value ?: emptyList()
//        _leftToPay.value = currentList + newItem
//        _markedToPay.value = _markedToPay.value?.filter { it.orderId != orderId }
//
//        val currentPriceLeft = _priceLeftToPay.value ?: 0.0
//        _priceLeftToPay.value = currentPriceLeft + newItem[0].price
//        val currentPriceToPay = _priceToPay.value ?: 0.0
//        _priceToPay.value = currentPriceToPay - newItem[0].price
//    }

    fun getLeftToPay(tableId: Int): LiveData<List<FrbOrderDTO>>{
        val itemsMLD = MutableLiveData<List<FrbOrderDTO>>()

        viewModelScope.launch(Dispatchers.IO){
            val byState = FrbFieldsOrders.States.CONFIRMED
            _ordersRealtimeRepository.getItemsRealtime(tableId, byState).collect() { list ->
                val priceLeftToPay = list.sumOf { it.price }
                withContext(Dispatchers.Main){
                    itemsMLD.value = list
                    _priceLeftToPay.value = priceLeftToPay
                }
            }
        }
        return itemsMLD
    }

    fun setTable(tableDTO: TableDTO){
        _selectedTable.postValue(tableDTO)
    }
//
//    fun onMoveItemToMarkedToPay(orderId: Int) {
//        val newItem = _leftToPay.value?.filter { it.orderId == orderId } ?: emptyList()
//        val currentList = _markedToPay.value ?: emptyList()
//        _markedToPay.value = currentList + newItem
//        _leftToPay.value = _leftToPay.value?.filter { it.orderId != orderId }
//
//        val currentPriceToPay = _priceToPay.value ?: 0.0
//        _priceToPay.value = currentPriceToPay + newItem[0].price
//        val currentPriceLeft = _priceLeftToPay.value ?: 0.0
//        _priceLeftToPay.value = currentPriceLeft - newItem[0].price
//    }
//
//    fun onMoveItemToLeftToPay(orderId: Int) {
//        val newItem = _markedToPay.value?.filter { it.orderId == orderId } ?: emptyList()
//        val currentList = _leftToPay.value ?: emptyList()
//        _leftToPay.value = currentList + newItem
//        _markedToPay.value = _markedToPay.value?.filter { it.orderId != orderId }
//
//        val currentPriceLeft = _priceLeftToPay.value ?: 0.0
//        _priceLeftToPay.value = currentPriceLeft + newItem[0].price
//        val currentPriceToPay = _priceToPay.value ?: 0.0
//        _priceToPay.value = currentPriceToPay - newItem[0].price
//    }

}