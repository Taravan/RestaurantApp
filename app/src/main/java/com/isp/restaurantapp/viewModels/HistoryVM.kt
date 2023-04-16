package com.isp.restaurantapp.viewModels

import androidx.lifecycle.*
import com.isp.restaurantapp.models.dto.FrbOrderDTO
import com.isp.restaurantapp.repositories.concrete.FrbRealTimeOrder
import com.isp.restaurantapp.repositories.interfaces.FrbRealtimeGetterService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HistoryVM: ViewModel() {
    // Realtime order repository
    private val _ordersRealtimeRepository: FrbRealtimeGetterService<FrbOrderDTO> by lazy{
        FrbRealTimeOrder()
    }

    fun getRealtimeOrders(uid: String): LiveData<List<FrbOrderDTO>>{
        val itemsMLD = MutableLiveData<List<FrbOrderDTO>>()

        viewModelScope.launch(Dispatchers.IO){
            _ordersRealtimeRepository.getItems(uid).collect() {
                withContext(Dispatchers.Main){
                    itemsMLD.value = it
                }
            }
        }
        return itemsMLD
    }
}