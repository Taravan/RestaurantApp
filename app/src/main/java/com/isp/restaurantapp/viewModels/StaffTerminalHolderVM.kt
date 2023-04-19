package com.isp.restaurantapp.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.isp.restaurantapp.models.Resource
import com.isp.restaurantapp.models.dto.FrbOrderDTO
import com.isp.restaurantapp.models.exceptions.RetrofitFailedException
import com.isp.restaurantapp.models.firebase.FrbFieldsOrders
import com.isp.restaurantapp.repositories.RepositoryDataMock
import com.isp.restaurantapp.repositories.RepositoryRetrofit
import com.isp.restaurantapp.repositories.concrete.FrbOrderStateUpdater
import com.isp.restaurantapp.repositories.concrete.FrbRealtimeByStateGetterServiceImpl
import com.isp.restaurantapp.repositories.interfaces.FrbDocumentStateUpdaterService
import com.isp.restaurantapp.repositories.interfaces.FrbRealtimeByStateGetterService
import com.isp.restaurantapp.repositories.interfaces.OrderInserterService
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

    //private val _repository = RepositoryDataMock()

    private val _orderRetrofitInserter: OrderInserterService by lazy {
        RepositoryRetrofit()
    }

    private val _orderFrbUpdater: FrbDocumentStateUpdaterService<FrbOrderDTO> by lazy {
        FrbOrderStateUpdater()
    }

    private val _ordersRealtimeRepository: FrbRealtimeByStateGetterService<FrbOrderDTO> by lazy {
        FrbRealtimeByStateGetterServiceImpl()
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
                    itemsMLD.value = it.sortedByDescending { it.lastUpdate }
                }
            }
        }
        return itemsMLD
    }

    /**
     * If a exception during background task occurs, set this value in exception handling
     * process. UI then can observe LiveData and show Toast with the error to the users
     */
    private val _errorState: MutableLiveData<String> by lazy{
        MutableLiveData<String>()
    }
    /**
     * Shows the error state that happened in the background tasks
     */
    val errorState: MutableLiveData<String>
        get() = _errorState


    private val _processedOrders = MutableLiveData<List<FrbOrderDTO>>()
    val processedOrders: LiveData<List<FrbOrderDTO>>
        get() = _processedOrders

    fun processPendingOrder(order: FrbOrderDTO) {
        viewModelScope.launch(Dispatchers.IO){
            try {
                val newId = insertToDatabase(order)
                insertToFirestore(order, newId)
            }
            catch (er: RetrofitFailedException){
                withContext(Dispatchers.Main){
                    Log.e(TAG, "Preparing error handling state")
                    _errorState.postValue("Db action failed. Abort")
                }
            }
        }
    }

    private suspend fun insertToDatabase(order: FrbOrderDTO): Int {
        val retroResult = _orderRetrofitInserter.insertOrder(
            order.price, order.uid, order.itemId, order.tableId
        )
        if (!retroResult.isSuccessful) {
            val message = "Order inseertion to MySQL failed!"
            Log.e(TAG, message)
            throw RetrofitFailedException(message)

        }
        val newId = retroResult.body()?.id ?: -1
        return newId
    }

    private suspend fun insertToFirestore(
        order: FrbOrderDTO,
        newId: Int
    ) {
        order.orderId = newId
        order.lastUpdate = Timestamp.now()
        order.state = FrbFieldsOrders.States.CONFIRMED.value

        Log.i(TAG, "new values: $order")
        Log.i(TAG, "Order inserted to MySQL with id $newId")

        val frbResult = _orderFrbUpdater.updateDocuments(listOf(order))
        when (frbResult) {
            is Resource.Success -> Log.i(TAG, "Order in firestore updated")
            is Resource.Failure -> Log.e(TAG, "Update in firestore failed: ${frbResult.exception}")
            Resource.Loading -> {}
        }
    }

    fun resetErrorState(){
        _errorState.postValue("")
    }

    fun processProcessedOrder(idOfOrder: Int) {
        _processedOrders.value = _processedOrders.value?.filter { it.orderId != idOfOrder }
    }
//
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