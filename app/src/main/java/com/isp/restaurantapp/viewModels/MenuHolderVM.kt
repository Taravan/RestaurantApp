package com.isp.restaurantapp.viewModels

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.isp.restaurantapp.coroutines.Coroutines
import com.isp.restaurantapp.models.Item
import com.isp.restaurantapp.models.MenuCategory
import com.isp.restaurantapp.repositories.dataMock
import kotlinx.coroutines.Job

class MenuHolderVM: ViewModel() {

    private lateinit var job: Job
    private val data: dataMock = dataMock()

    private val _menuItems = MutableLiveData<List<Item>>()
    val menuItems: LiveData<List<Item>> = _menuItems

    val menuCategories: LiveData<List<MenuCategory>> = Transformations.map(menuItems) { items ->
        items.groupBy { it.categoryId }
            .map { MenuCategory(it.value.first().categoryName, it.value) }
    }




    fun getCategories() {

        job = Coroutines.ioTheMain(
            { data.getItems() },
            { _menuItems.value = it }
        )

    }

    override fun onCleared() {
        super.onCleared()
        if (::job.isInitialized) job.cancel()
    }

    init {
        getCategories()
    }

}