package com.isp.restaurantapp.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.isp.restaurantapp.models.Resource
import com.isp.restaurantapp.models.dto.FrbOrderDTO
import com.isp.restaurantapp.models.exceptions.OrderNotPendingDeleteException
import com.isp.restaurantapp.models.firebase.FrbFieldsOrders
import com.isp.restaurantapp.repositories.concrete.FrbOrderDeleter
import com.isp.restaurantapp.repositories.concrete.FrbOrderStateUpdater
import com.isp.restaurantapp.repositories.concrete.FrbRealtimeOrder
import com.isp.restaurantapp.repositories.interfaces.FrbDocumentDeleter
import com.isp.restaurantapp.repositories.interfaces.FrbDocumentStateUpdaterService
import com.isp.restaurantapp.repositories.interfaces.FrbRealtimeGetterByTableIdService
import kotlinx.coroutines.*

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

    private val _orderDeleter: FrbDocumentDeleter<FrbOrderDTO> by lazy {
        FrbOrderDeleter()
    }



    private var _selectedItemsToPay = mutableListOf<FrbOrderDTO>()
    val selectedItemsToPay: List<FrbOrderDTO>
        get() = _selectedItemsToPay


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

        // ensures that all items have the same timestamp that can be used as payment request id :)
        val uniqueTimestamp = Timestamp.now()
        _selectedItemsToPay.forEach {
            it.state = FrbFieldsOrders.States.FOR_PAYMENT.value
            it.uid = uid
            it.lastUpdate = uniqueTimestamp
        }

        viewModelScope.launch(Dispatchers.IO){
            val result = _orderUpdater.updateDocuments(_selectedItemsToPay)
            withContext(Dispatchers.Main){
                when(result){
                    is Resource.Failure -> throw result.exception
                    is Resource.Success -> _selectedItemsToPay.clear()
                    else -> {}
                }
            }
        }

    }

    fun deletePendingOrder(order: FrbOrderDTO){
        val handler = CoroutineExceptionHandler{ _, throwable ->
            throwable.printStackTrace()
        }
        viewModelScope.launch(Dispatchers.IO){
            try {
                if (order.state != FrbFieldsOrders.States.PENDING.value)
                    throw OrderNotPendingDeleteException()
                when (val result = _orderDeleter.deleteDocument(order)){
                    is Resource.Failure -> throw result.exception
                    is Resource.Success -> Log.i(
                        TAG,
                        "deletePendingOrder: Order deleted successfully"
                    )
                    Resource.Loading -> {}
                }
            }
            catch (e: Exception){
                Log.e(TAG, "deletePendingOrder: ${e.message}")
            }
        }
    }

}