package com.isp.restaurantapp.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isp.restaurantapp.models.dto.*
import com.isp.restaurantapp.repositories.RepositoryAbstract
import com.isp.restaurantapp.repositories.RepositoryRetrofit
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.log

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

    private val _errorException: MutableLiveData<Exception?> by lazy{
        MutableLiveData<Exception?>()
    }
    val errorException: LiveData<Exception?>
        get() = _errorException

    fun resetErrorState(){
        _errorState.postValue("")
        _errorException.postValue(null)
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

        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.i(TAG, "addTable: initing table insertion")
                val number: Int = tableNumber.toInt()
                val result = _repository.insertTable(number, qrCode)
                Log.i(TAG, "addTable: isSsucessful = ${result.isSuccessful}, tableNumber = $number")
                if (!result.isSuccessful) throw Exception("Table insertion failed")
                withContext(Dispatchers.Main) {
                    Log.i(
                        TAG,
                        "addTable: adding table complete, ${result.body()?.id.toString()} rows effected"
                    )
                    Log.e(TAG, "Adding $tableNumber with qr code $qrCode .")
                }
            }
            catch (e: Exception){
                Log.e(TAG, "addTable: Error while inserting table ${e.message}")
                e.printStackTrace()
            }
        }
    }

    fun updateTable(tableId: Int, tableNumber: String, qrCode: String) {
        val tableToUpdate = (_tables.value?.find { it.id == tableId } ?: "") as TableDTO
        Log.e(TAG, "Updating table number: ${tableToUpdate.tableNumber.toString()}.")
    }

    fun deleteTable(tableId: Int) {
        val tableToDelete = (_tables.value?.find { it.id == tableId } ?: "") as TableDTO
        Log.e(TAG, "Deleting table number: ${tableToDelete.tableNumber.toString()}.")
    }

    /**
     * Categories
     */
    private val _categories = MutableLiveData<List<CategoryDTO>>()
    val categories: LiveData<List<CategoryDTO>>
        get() = _categories

        fun fetchCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = _repository.getCategories()
                withContext(Dispatchers.Main){
                    _categories.postValue(result.body())
                }
            } catch (e: Exception){
                withContext(Dispatchers.Main){
                    Log.e(TAG, e.message.toString())
                    e.printStackTrace()
                    _errorState.postValue("Error occurred while fetching categories from database")
                }
            }
        }
    }

    fun addCategory(categoryName: String) {
        val handler = CoroutineExceptionHandler{ _, e ->
            e.printStackTrace()
        }

        viewModelScope.launch(Dispatchers.IO + handler) {
            try {
                val result = _repository.insertCategory(categoryName, null)
                if (!result.isSuccessful) throw Exception("Category insertion failed")
            } catch (e: Exception){
                Log.e(TAG, "addCategory: category insertion failed")
                e.printStackTrace()
            }
        }
        Log.i(TAG, "Adding $categoryName category.")
    }

    fun updateCategory(categoryId: Int) {
        val categoryToUpdate = (_categories.value?.find { it.id ==categoryId } ?: "") as CategoryDTO
        Log.e(TAG, "Updating category: ${categoryToUpdate.name}.")
    }

    fun deleteCategory(categoryId: Int) {
        val categoryToDelete = (_categories.value?.find { it.id ==categoryId } ?: "") as CategoryDTO
        Log.e(TAG, "Deleting category: ${categoryToDelete.name}.")
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
        val productToUpdate = (_goods.value?.find { it.goodsId == productId } ?: "") as GoodsItemDTO
        Log.e(TAG, "Updating product: ${productToUpdate.goodsName}.")
    }

    fun deleteProduct(productId: Int) {
        val productToDelete = (_goods.value?.find { it.goodsId == productId } ?: "") as GoodsItemDTO
        Log.e(TAG, "Deleting product: ${productToDelete.goodsName}.")
    }

    init {

        _categories.value = listOf(
            CategoryDTO(0, "Rizky", null),
            CategoryDTO(1, "Piva", null),
            CategoryDTO(2, "Nealko", null)
        )

        _allergens.value = listOf(
            AllergenDTO(0, "Lepek"),
            AllergenDTO(1, "Mléko"),
            AllergenDTO(2, "Skořápky")
        )


    }



}