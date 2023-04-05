package com.isp.restaurantapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.isp.restaurantapp.coroutines.Coroutines
import com.isp.restaurantapp.models.Item
import com.isp.restaurantapp.repositories.dataMock
import kotlinx.coroutines.Job

class MenuVM(private val data: dataMock) : ViewModel() {

    private lateinit var job: Job

    private val _item = MutableLiveData<Item>()
    val item: LiveData<Item?>
        get() = _item

    private val _items = MutableLiveData<List<Item>>()
    val items: LiveData<List<Item>>
        get() = _items




    fun getItems() {
        job = Coroutines.ioTheMain(
            { data.getItems() },
            { _items.value = it }
        )
    }

    override fun onCleared() {
        super.onCleared()
        if (::job.isInitialized) job.cancel()
    }

    init {
        getItems()
    }

}