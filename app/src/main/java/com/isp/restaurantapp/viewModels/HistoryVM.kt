package com.isp.restaurantapp.viewModels

import androidx.lifecycle.*
import com.isp.restaurantapp.models.dto.FrbOrderDTO
import com.isp.restaurantapp.repositories.concrete.FrbRealTimeOrderPaid
import com.isp.restaurantapp.repositories.interfaces.FrbRealtimeGetterByUidService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HistoryVM: ViewModel() {
    // Realtime order repository
    private val _ordersRealtimeRepository: FrbRealtimeGetterByUidService<FrbOrderDTO> by lazy{
        FrbRealTimeOrderPaid()
    }

    fun getRealtimeOrders(uid: String): LiveData<List<FrbOrderDTO>>{
        val itemsMLD = MutableLiveData<List<FrbOrderDTO>>()

        viewModelScope.launch(Dispatchers.IO){
            _ordersRealtimeRepository.getItemsRealtime(uid).collect() {
                withContext(Dispatchers.Main){
                    itemsMLD.value = it
                }
            }
        }
        return itemsMLD
    }
}