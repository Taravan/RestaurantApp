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
import com.isp.restaurantapp.repositories.concrete.FrbRealtimeByStateGetterServiceImpl
import com.isp.restaurantapp.repositories.concrete.FrbRealtimeGetterByTableIdAndStateServiceImpl
import com.isp.restaurantapp.repositories.concrete.FrbRealtimeOrderABC
import com.isp.restaurantapp.repositories.interfaces.FrbDocumentDeleter
import com.isp.restaurantapp.repositories.interfaces.FrbRealtimeByStateGetterService
import com.isp.restaurantapp.repositories.interfaces.FrbRealtimeGetterService
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StaffOverviewVM: ViewModel() {

    private val _repositoryAll: FrbRealtimeGetterService<FrbOrderDTO> = FrbRealtimeOrderABC()
    private val _repositoryPaid: FrbRealtimeByStateGetterService<FrbOrderDTO> = FrbRealtimeByStateGetterServiceImpl()

    private val _errorException: MutableLiveData<Throwable?> by lazy{
        MutableLiveData<Throwable?>()
    }
    val errorException: LiveData<Throwable?>
        get() = _errorException
    fun resetErrorException(){
        _errorException.postValue(null)
    }

    private val _orderDeleter: FrbDocumentDeleter<FrbOrderDTO> by lazy {
        FrbOrderDeleter()
    }



    fun getRealtimeOrdersAll(): LiveData<List<FrbOrderDTO>>{
        val itemsMLD = MutableLiveData<List<FrbOrderDTO>>()

        viewModelScope.launch(Dispatchers.IO){
            _repositoryAll.getItemsRealtime().collect() {
                val sorted = it.sortedBy {  it.state }
                withContext(Dispatchers.Main){
                    itemsMLD.value = sorted
                }
            }
        }
        return itemsMLD
    }
    fun getRealtimeOrdersPaid(): LiveData<List<FrbOrderDTO>>{
        val itemsMLD = MutableLiveData<List<FrbOrderDTO>>()

        val byState = FrbFieldsOrders.States.PAID
        viewModelScope.launch(Dispatchers.IO){
            _repositoryPaid.getItemsRealtime(byState).collect() {
                withContext(Dispatchers.Main){
                    itemsMLD.value = it
                }
            }
        }
        return itemsMLD
    }

    fun deletePendingOrder(order: FrbOrderDTO){
        val handler = CoroutineExceptionHandler{ _, throwable ->
            if (throwable is OrderNotPendingDeleteException)
                _errorException.postValue(throwable)
            throwable.printStackTrace()
        }
        viewModelScope.launch(Dispatchers.IO + handler){
            try {
                if (order.state != FrbFieldsOrders.States.PENDING.value)
                    throw OrderNotPendingDeleteException()
                when (val result = _orderDeleter.deleteDocument(order)){
                    is Resource.Failure -> throw result.exception
                    is Resource.Success -> Log.i(
                        PayVM.TAG,
                        "deletePendingOrder: Order deleted successfully"
                    )
                    Resource.Loading -> {}
                }
            }
            catch (e: Exception){
                Log.e(PayVM.TAG, "deletePendingOrder: ${e.message}")
                throw e
            }
        }
    }
}