package com.isp.restaurantapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.isp.restaurantapp.models.dto.FrbOrderDTO
import com.isp.restaurantapp.repositories.concrete.FrbRealtimeOrderABC
import com.isp.restaurantapp.repositories.interfaces.FrbRealtimeGetterService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StaffOverviewVM: ViewModel() {

    private val _repository: FrbRealtimeGetterService<FrbOrderDTO> = FrbRealtimeOrderABC()

    private val _allOrders = MutableLiveData<List<FrbOrderDTO>>()
    val allOrders: LiveData<List<FrbOrderDTO>>
        get() = _allOrders

    fun getRealtimeOrdersAll(): LiveData<List<FrbOrderDTO>>{
        val itemsMLD = MutableLiveData<List<FrbOrderDTO>>()

        viewModelScope.launch(Dispatchers.IO){
            _repository.getItemsRealtime().collect() {
                val sorted = it.sortedBy {  it.state }
                withContext(Dispatchers.Main){
                    itemsMLD.value = sorted
                }
            }
        }
        return itemsMLD
    }

    init {
        _allOrders.value = listOf(
            FrbOrderDTO("0", 0, 0, "ObjJedna", Timestamp.now(), 0.0, 0, "Done", 0, 0, "0"),
            FrbOrderDTO("1", 1, 1, "ObjDva", Timestamp.now(), 0.1, 1, "Pending", 1, 1, "1")
        )
    }

}