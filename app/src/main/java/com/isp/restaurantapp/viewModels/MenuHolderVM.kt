package com.isp.restaurantapp.viewModels

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.*
import com.isp.restaurantapp.coroutines.Coroutines
import com.isp.restaurantapp.models.Item
import com.isp.restaurantapp.models.MenuCategory
import com.isp.restaurantapp.repositories.DataMock
import kotlinx.coroutines.Job

class MenuHolderVM(application: Application): AndroidViewModel(application) {

    private lateinit var job: Job
    private val data: DataMock = DataMock()

    private val _menuItems = MutableLiveData<List<Item>>()
    val menuItems: LiveData<List<Item>> = _menuItems

    val menuCategories: LiveData<List<MenuCategory>> = _menuItems.map() { items ->
        items.groupBy { it.categoryId }
            .map { MenuCategory(it.value.first().categoryName, it.value) }
    }

    fun getCategories() {

        job = Coroutines.ioTheMain(
            { data.getItems() },
            { _menuItems.value = it }
        )

    }

    fun orderButtonClicked(item: Item) {
        Toast.makeText(getApplication(), "Buy item Id: " + item.id.toString() + " " + item.name + " for: " + item.price.toString() + " Kč.", Toast.LENGTH_LONG).show()
    }

    override fun onCleared() {
        super.onCleared()
        if (::job.isInitialized) job.cancel()
    }

    init {
        getCategories()
    }

}