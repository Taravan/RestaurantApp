package com.isp.restaurantapp.viewModels

import android.util.Log
import androidx.lifecycle.*
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
        get() = _allergens.map { it.sortedBy { it.id } }

    private val _selectedAllergens: MutableLiveData<MutableSet<AllergenDTO>> by lazy{
        MutableLiveData<MutableSet<AllergenDTO>>(mutableSetOf())
    }
    val selectedAllergens: LiveData<MutableSet<AllergenDTO>>
        get() = _selectedAllergens

    fun resetSelectedAllergens() {
        _selectedAllergens.value?.clear()
    }

    fun updateSelectedList(operation: Boolean, allergen: AllergenDTO) {
        if (operation){
            Log.i(TAG, "updateSelectedList: added $allergen")
            _selectedAllergens.value?.add(allergen)
        }
        else {
            Log.i(TAG, "updateSelectedList: removed $allergen")
            _selectedAllergens.value?.remove(allergen)
        }
    }

    fun isAllergen(allergen: AllergenDTO): Boolean{
        return _selectedAllergens.value?.contains(allergen) ?: false
    }


    /**
     * Tables
     */
    private var updatedTableId: Int = 0
    val updatedTableNumber = MutableLiveData<String>()
    val updatedTableQrCode = MutableLiveData<String>()

    fun setUpdatedTable(table: TableDTO) {
        updatedTableId = table.id
        updatedTableNumber.value = table.tableNumber.toString()
        updatedTableQrCode.value = table.qrCode
    }


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

    fun updateTable() {
        val tableToUpdate = (_tables.value?.find { it.id == updatedTableId} ?: "") as TableDTO
        Log.e(TAG, "Updating table: id: ${tableToUpdate.id.toString()} -> ${updatedTableId.toString()} "+
                "number: ${tableToUpdate.tableNumber.toString()} -> ${updatedTableNumber.value} " +
                "and qr: ${tableToUpdate.qrCode} -> ${updatedTableQrCode.value}.")
    }

    fun deleteTable(tableId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.i(TAG, "addTable: initing table delete")
                val result = _repository.deleteTable(tableId)
                if (!result.isSuccessful) throw Exception("Table insertion failed")
                withContext(Dispatchers.Main) {
                    Log.i(
                        TAG,
                        "deleteTable: delete table complete, ${result.body()?.id.toString()} rows effected"
                    )
                }
            }
            catch (e: Exception){
                Log.e(TAG, "deleteTable: Error while deleting table ${e.message}")
                e.printStackTrace()
            }
        }
    }

    /**
     * Categories
     */

//    private val _updatedCatId = MutableLiveData<Int>()
//    private val updatedCatId: LiveData<Int>
//        get() = _updatedCatId
//
//    private val _updatedCatName = MutableLiveData<String>()
//    val updatedCatName: LiveData<String>
//        get() = _updatedCatName


    private var updatedCatId: Int = 0
    val updatedCatName = MutableLiveData<String>()

    fun setUpdatedCategory(category: CategoryDTO) {
        updatedCatId = category.id
        updatedCatName.value = category.name
    }

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

    fun updateCategory() {
        val categoryToUpdate = (_categories.value?.find { it.id == updatedCatId } ?: "") as CategoryDTO
        Log.e(TAG, "Updating category: id: ${categoryToUpdate.id.toString()} -> ${updatedCatId.toString()} "+
                "name: ${categoryToUpdate.name.toString()} -> ${updatedCatName.value}.")
    }

    fun deleteCategory(categoryId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.i(TAG, "deleteCategory: initing table category")
                val result = _repository.deleteCategory(categoryId)
                if (!result.isSuccessful) throw Exception("Table insertion failed")
                withContext(Dispatchers.Main) {
                    Log.i(
                        TAG,
                        "deleteCategory: delete category complete, ${result.body()?.id.toString()} rows effected"
                    )
                }
            }
            catch (e: Exception){
                Log.e(TAG, "deleteCategory: Error while deleting category ${e.message}")
                e.printStackTrace()
            }
        }
    }


    /**
     * Products
     */
    private val _goods = MutableLiveData<List<GoodsItemDTO>>()
    val goods: LiveData<List<GoodsItemDTO>>
        get() = _goods.map {
            it.distinctBy {
                it.goodsId
            }
        }

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

    fun fetchAllergens(){
        viewModelScope.launch(Dispatchers.IO){
            try {
                val result = _repository.getAllergens()
                withContext(Dispatchers.Main){
                    _allergens.postValue(result.body())
                }
            } catch (e: Exception){
                Log.e(TAG, "fetchAllergens: Error fetching allergens" )
                e.printStackTrace()
            }
        }
    }

    fun addProduct(name: String, desc: String?, category: CategoryDTO, price: String, allergens: List<AllergenDTO>) {
        Log.i(TAG, "Adding name=$name , cat=${category.id} , allergens=$allergens")
        viewModelScope.launch(Dispatchers.IO){
            try {
                val allergenIds = allergens.map {
                    it.id
                }
                val priceDouble = price.toDouble()
                val data = InsertGoodsItemDTO(name, desc, category.id, priceDouble, allergenIds)
                val result = _repository.insertGoodsItemWithAllergens(data)
                withContext(Dispatchers.Main){
                    Log.i(TAG, "addProduct: new product added with id ${result.body()?.id}")
                }

            } catch (e: Exception){
                Log.e(TAG, "addProduct: adding goods item failed with exception: $e")
                e.printStackTrace()
            }
        }

    }

    fun updateProduct(productId: Int) {
        val productToUpdate = (_goods.value?.find { it.goodsId == productId } ?: "") as GoodsItemDTO
        Log.e(TAG, "Updating product: ${productToUpdate.goodsName}.")
    }

    fun deleteProduct(productId: Int) {
        val productToDelete = (_goods.value?.find { it.goodsId == productId } ?: "") as GoodsItemDTO
        Log.e(TAG, "Deleting product: ${productToDelete.goodsName}.")
    }



}