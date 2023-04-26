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
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlin.math.log

class StaffGoodsVM: ViewModel() {

    companion object {
        private const val TAG = "StaffGoodsVM"
    }


    var prefix: String = ""
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

    private val _errorException: MutableLiveData<Throwable?> by lazy{
        MutableLiveData<Throwable?>()
    }
    val errorException: LiveData<Throwable?>
        get() = _errorException

    fun resetErrorState(){
        _errorState.postValue("")
        _errorException.postValue(null)
    }



    private val _allergens = MutableLiveData<List<AllergenDTO>>()
    val allergens: LiveData<List<AllergenDTO>>
        get() = _allergens.map { list -> list.sortedBy { it.id } }

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
                val tables = _repository.getTables().sortedBy { it.tableNumber }
                // Delete prefix URL
                Log.i(TAG, "fetchTables: prefix:$prefix")
                tables.forEach {
                    it.qrCode = it.qrCode.split(RepositoryRetrofit.CODE)[2]
                    Log.i(TAG, "fetchTables: newQr: ${it.qrCode}")
                }
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
                val newQr = getQrWithPrefix(qrCode)
                val result = _repository.insertTable(number, newQr)

                Log.i(TAG, "addTable: isSsucessful = ${result.isSuccessful}, tableNumber = $number")
                if (!result.isSuccessful) throw Exception("Table insertion failed")
                fetchTables()
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
        Log.i(TAG, "Updating table: id: ${tableToUpdate.id.toString()} -> ${updatedTableId.toString()} "+
                "number: ${tableToUpdate.tableNumber.toString()} -> ${updatedTableNumber.value} " +
                "and qr: ${tableToUpdate.qrCode} -> ${updatedTableQrCode.value}.")

        val handler = CoroutineExceptionHandler{_, throwable ->
            _errorException.postValue(throwable)
            throwable.printStackTrace()
        }

        val newNumber: Int = updatedTableNumber.value?.toInt() ?: 0
        val newQr: String = getQrWithPrefix(updatedTableQrCode.value.toString())

        viewModelScope.launch(Dispatchers.IO + handler){
            _repository.updateTable(
                updatedTableId,
                newNumber,
                newQr
            )
            fetchTables()
        }
    }

    fun deleteTable(tableId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.i(TAG, "addTable: initing table delete")
                val result = _repository.deleteTable(tableId)
                if (!result.isSuccessful) throw Exception("Table insertion failed")
                fetchTables()
            }
            catch (e: Exception){
                Log.e(TAG, "deleteTable: Error while deleting table ${e.message}")
                e.printStackTrace()
            }
        }
    }

    private fun getQrWithPrefix(qrCode: String): String {
        return prefix + qrCode
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
                fetchCategories()
            } catch (e: Exception){
                Log.e(TAG, "addCategory: category insertion failed")
                e.printStackTrace()
            }
        }
        Log.i(TAG, "Adding $categoryName category.")
    }

    fun updateCategory() {
        val categoryToUpdate = (_categories.value?.find { it.id == updatedCatId } ?: "") as CategoryDTO
        Log.e(TAG, "Updating category: id: ${categoryToUpdate.id} -> $updatedCatId "+
                "name: ${categoryToUpdate.name} -> ${updatedCatName.value}.")


        val handler = CoroutineExceptionHandler{ _, throwable ->
            _errorException.postValue(throwable)
            throwable.printStackTrace()
        }

        viewModelScope.launch(Dispatchers.IO + handler){
            _repository.updateCategory(
                updatedCatId,
                updatedCatName.value.toString(),
                categoryToUpdate.description
            )
            fetchCategories()
        }
    }

    fun deleteCategory(categoryId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.i(TAG, "deleteCategory: initing table category")
                val result = _repository.deleteCategory(categoryId)
                if (!result.isSuccessful) throw Exception("Table insertion failed")
                fetchCategories()
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


    lateinit var productToUpdate: GoodsItemDTO
    private val _fetchedProductsAllergens = MutableLiveData<MutableSet<AllergenDTO>>()
    val fetchedProductsAllergens: LiveData<MutableSet<AllergenDTO>>
        get() = _fetchedProductsAllergens

    private var updatedProductGoodsId: Int = 0
    val updatedProductName = MutableLiveData<String>()
    val updatedProductDesc = MutableLiveData<String>()
    val updatedProductCatPosition = MutableLiveData<Int>()
    lateinit var updatedProductCategory :CategoryDTO
    private val _updatedProductAllergens = MutableLiveData<MutableSet<AllergenDTO>>()
    val updatedProductPrice = MutableLiveData<String>()

    fun updateSelectedListForUpdate(operation: Boolean, allergen: AllergenDTO) {
        if (operation){
            Log.i(TAG, "updateSelectedList: added $allergen")
            _updatedProductAllergens.value?.add(allergen)
        }
        else {
            Log.i(TAG, "updateSelectedList: removed $allergen")
            _updatedProductAllergens.value?.remove(allergen)
        }
    }

    fun isAllergenForUpdate(allergen: AllergenDTO): Boolean{
        return _updatedProductAllergens.value?.contains(allergen) ?: false
    }

    fun setUpdatedProduct() {
        updatedProductGoodsId = productToUpdate.goodsId
        updatedProductName.value = productToUpdate.goodsName
        updatedProductDesc.value = productToUpdate.goodsDesc ?: ""
        updatedProductCatPosition.value = _categories.value?.indexOfFirst { it.id == productToUpdate.categoryId } ?: 0
        _updatedProductAllergens.value = _fetchedProductsAllergens.value
        updatedProductPrice.value = productToUpdate.price.toString()
    }

    private val _goods = MutableLiveData<List<GoodsItemDTO>>()
    val goods: LiveData<List<GoodsItemDTO>>
        get() = _goods.map { list ->
            list.distinctBy {
                it.goodsId
            }.sortedBy { it.categoryId }
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

    fun fetchAllergensForGoodsItem(productId: Int) {
        viewModelScope.launch(Dispatchers.IO){
            try {
                val result = _repository.getAllergensForGoodsItem(productId)
                withContext(Dispatchers.Main){
                    _fetchedProductsAllergens.postValue(result.body()?.toMutableSet())
                }
            } catch (e: Exception){
                Log.e(TAG, "fetchAllergensForGoodsItem: Error fetching allergens" )
                e.printStackTrace()
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
        val handler = CoroutineExceptionHandler{ _, e ->
            if (e is NumberFormatException){
                _errorException.postValue(e)
            }
            e.printStackTrace()
        }

        Log.i(TAG, "Adding name=$name , cat=${category.id} , allergens=$allergens")
        viewModelScope.launch(Dispatchers.IO + handler){
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
                fetchGoods()

            } catch (e: NumberFormatException){
                Log.e(TAG, "addProduct: adding goods item failed with exception: $e")
                throw e
            } catch (e: Exception){
                Log.e(TAG, "addProduct: adding goods item failed with exception: $e")
                throw e
            }
        }

    }

    fun updateProduct() {
        val handler = CoroutineExceptionHandler{ _, throwable ->
            if (throwable is NumberFormatException)
                Log.i(TAG, "updateProduct: Wrong number format")
            _errorException.postValue(throwable)
            throwable.printStackTrace()
        }

        val productToUpdate = (_goods.value?.find { it.goodsId == updatedProductGoodsId } ?: "") as GoodsItemDTO


        viewModelScope.launch(IO + handler){
            try {
                val newPrice: Double = updatedProductPrice.value.toString().toDouble()

                Log.e(TAG, "Updating product: id: ${productToUpdate.goodsId} -> ${updatedProductGoodsId}, " +
                        "name: ${productToUpdate.goodsName} -> ${updatedProductName.value} " +
                        ", desc: ${productToUpdate.goodsDesc} -> ${updatedProductDesc.value}, " +
                        "cat: ${categories.value?.find { it.id == productToUpdate.categoryId }?.name} -> ${updatedProductCategory.name}, " +
                        "price: ${productToUpdate.price} -> ${updatedProductPrice.value} " +
                        "allergens: ${_updatedProductAllergens.value?.size}.")
                val newProduct = UpdateGoodsItemDTO(
                    updatedProductGoodsId,
                    updatedProductName.value.toString(),
                    updatedProductDesc.value,
                    updatedProductCategory.id,
                    newPrice,
                    _updatedProductAllergens.value.orEmpty().map { it.id }
                )

                val result = _repository.updateGoodsItemWithAllergens(newProduct)
                Log.i(TAG, "updateProduct: rows effected: ${result.body()?.id}")
                fetchGoods()

            }
            catch (e: Exception){
                throw e
            }
        }
    }

    fun deleteProduct(productId: Int) {
        Log.e(TAG, "Deleting product.")
        val handler = CoroutineExceptionHandler{ _, e ->
            _errorState.postValue("Something went wrong")
            e.printStackTrace()
        }

        viewModelScope.launch(Dispatchers.IO + handler){
            val result = _repository.deleteGoodsItem(productId)
            fetchGoods()
        }
    }



}