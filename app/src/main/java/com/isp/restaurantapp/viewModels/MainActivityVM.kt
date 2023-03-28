package com.isp.restaurantapp.viewModels

import android.util.Log
import androidx.lifecycle.*
import com.isp.restaurantapp.models.Table
import com.isp.restaurantapp.models.User
import com.isp.restaurantapp.repositories.MyApi
import com.isp.restaurantapp.repositories.RetrofitService
import kotlinx.coroutines.launch
import retrofit2.awaitResponse

class MainActivityVM: ViewModel() {

    private val _selectedItem = MutableLiveData<Table?>()
    val selectedItem: LiveData<Table?>
        get() = _selectedItem

    private val retrofitService by lazy {
        RetrofitService()
    }

    private var tables: MutableLiveData<List<Table>> = MutableLiveData<List<Table>>()


    // getter, could be defined as a LiveData property getter as well
    fun getTablesLiveData(): LiveData<List<Table>>{
        return tables
    }


    val name: MutableLiveData<String> by lazy {
        MutableLiveData<String>("Nekdo")
    }

    val email: MutableLiveData<String> by lazy {
        MutableLiveData<String>("nekdo@seznam.cz")
    }

    init {
        fetchTablesFromApi()
    }

    /**
     * Call whenever API response is needed
     * Fills [MutableLiveData] property [tables] with values from database
     */
    fun fetchTablesFromApi(){
        viewModelScope.launch {
            try {
                val response = retrofitService.apiService.getTables()
                tables.value = response
            }
            catch (e: Exception) {
                Log.e("MainViewModel", "Error getting tables", e)
            }
        }
    }

    fun onItemClick(table: Table){
        _selectedItem.value = table
    }

    fun onItemClickComplete(){
        _selectedItem.value = null
    }

}