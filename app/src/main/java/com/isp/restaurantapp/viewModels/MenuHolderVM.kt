package com.isp.restaurantapp.viewModels

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.*
import com.isp.restaurantapp.coroutines.Coroutines
import com.isp.restaurantapp.models.MenuItem
import com.isp.restaurantapp.models.ItemCategory
import com.isp.restaurantapp.repositories.RepositoryDataMock
import kotlinx.coroutines.Job

class MenuHolderVM(application: Application): AndroidViewModel(application) {

    private lateinit var job: Job
    private val data: RepositoryDataMock = RepositoryDataMock()

    private val _menuItems = MutableLiveData<List<MenuItem>>()
    val menuItems: LiveData<List<MenuItem>> = _menuItems

    val menuCategories: LiveData<List<ItemCategory>> = _menuItems.map() { items ->
        items.groupBy { it.categoryId }
            .map { ItemCategory(it.value.first().categoryName, it.value) }
    }

    fun getCategories() {

        job = Coroutines.ioTheMain(
            { data.getItems() },
            { _menuItems.value = it }
        )

    }

    fun orderButtonClicked(menuItem: MenuItem) {
        Toast.makeText(getApplication(), "Buy item Id: " + menuItem.id.toString() + " " + menuItem.name + " for: " + menuItem.price.toString() + " Kč.", Toast.LENGTH_LONG).show()
    }

    override fun onCleared() {
        super.onCleared()
        if (::job.isInitialized) job.cancel()
    }

    init {
        getCategories()
    }

}