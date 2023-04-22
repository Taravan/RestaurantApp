package com.isp.restaurantapp.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isp.restaurantapp.models.dto.*
import com.isp.restaurantapp.repositories.RepositoryAbstract
import com.isp.restaurantapp.repositories.RepositoryRetrofit
import com.isp.restaurantapp.repositories.concrete.FrbRealtimeGetterByTableIdAndStateServiceImpl
import com.isp.restaurantapp.repositories.interfaces.FrbRealtimeGetterByTableIdAndStateService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StaffGoodsVM: ViewModel() {

    companion object {
        const val TAG = "StaffGoodsVM"
    }

//    private val _ordersRealtimeRepository: FrbRealtimeGetterByTableIdAndStateService<FrbOrderDTO>
//            by lazy {
//                FrbRealtimeGetterByTableIdAndStateServiceImpl()
//            }

    /**
     * Repository for database actions, eg. Tables getter
     */
    private val _repository: RepositoryAbstract by lazy {
        //RepositoryDataMock()
        RepositoryRetrofit()
    }

    private val _errorState: MutableLiveData<String> by lazy{
        MutableLiveData<String>()
    }
    val errorState: LiveData<String>
        get() = _errorState

    fun resetErrorState(){
        _errorState.postValue("")
    }

    private val _allergens = MutableLiveData<List<AllergenDTO>>()
    val allergens: LiveData<List<AllergenDTO>>
        get() = _allergens

    private var _selectedAllergens = mutableListOf<AllergenDTO>()
    val selectedAllergens: List<AllergenDTO>
        get() = _selectedAllergens

    fun resetSelectedAllergens() {
        _selectedAllergens.clear()
    }

    fun updateSelectedList(operation: Boolean, allergen: AllergenDTO) {
        if (operation) _selectedAllergens.add(allergen)
        else _selectedAllergens.remove(allergen)
    }


    /**
     * Tables
     */
    private val _tables = MutableLiveData<List<TableDTO>>()
    val tables: LiveData<List<TableDTO>>
        get() = _tables

    fun fetchTables() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val tables = _repository.getTables()
                withContext(Dispatchers.Main){
                    _tables.postValue(tables)
                }
            } catch (e: Exception){
                withContext(Dispatchers.Main){
                    Log.e(TAG, e.message.toString())
                    _errorState.postValue("Error occurred while fetching tables from database")
                }
            }
        }
    }

    fun addTable(tableNumber: String, qrCode: String) {
        Log.e(TAG, "Adding $tableNumber with qr code $qrCode .")
    }

    fun updateTable(tableId: Int) {

    }

    fun deleteTable(tableId: Int) {

    }

    /**
     * Categories
     */
    private val _categories = MutableLiveData<List<CategoryDTO>>()
    val categories: LiveData<List<CategoryDTO>>
        get() = _categories

//        fun fetchCategories() {
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                val categories = _repository.getCategories()
//                withContext(Dispatchers.Main){
//                    _categories.postValue(categories)
//                }
//            } catch (e: Exception){
//                withContext(Dispatchers.Main){
//                    Log.e(TAG, e.message.toString())
//                    _errorState.postValue("Error occurred while fetching categories from database")
//                }
//            }
//        }
//    }

    fun addCategory(categoryName: String) {
        Log.e(TAG, "Adding $categoryName category.")
    }

    fun updateCategory(categoryId: Int) {

    }

    fun deleteCategory(categoryId: Int) {

    }


    /**
     * Products
     */
    private val _goods = MutableLiveData<List<GoodsItemDTO>>()
    val goods: LiveData<List<GoodsItemDTO>>
        get() = _goods

    fun fetchGoods() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val goods = _repository.getGoods()
                withContext(Dispatchers.Main){
                    _goods.postValue(goods)
                }
            } catch (e: Exception){
                withContext(Dispatchers.Main){
                    Log.e(TAG, e.message.toString())
                    _errorState.postValue("Error occurred while fetching goods from database")
                }
            }
        }
    }

    fun addProduct(name: String, desc: String, category: CategoryDTO, allergens: List<AllergenDTO>) {
        Log.e(TAG, "Adding $name , ${category.id.toString()} , ${allergens.size.toString()}")
    }

    fun updateProduct(productId: Int) {

    }

    fun deleteProduct(productId: Int) {

    }

    init {

        _categories.value = listOf(
            CategoryDTO(0, "Rizky"),
            CategoryDTO(1, "Piva"),
            CategoryDTO(2, "Nealko")
        )

        _allergens.value = listOf(
            AllergenDTO(0, "Lepek"),
            AllergenDTO(1, "Mléko"),
            AllergenDTO(2, "Skořápky")
        )


    }



}