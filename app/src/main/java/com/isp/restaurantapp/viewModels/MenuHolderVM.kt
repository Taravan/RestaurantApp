package com.isp.restaurantapp.viewModels

import android.security.keystore.UserNotAuthenticatedException
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.isp.restaurantapp.models.FrbOrderMapper
import com.isp.restaurantapp.models.ItemCategory
import com.isp.restaurantapp.models.Resource
import com.isp.restaurantapp.models.dto.AllergenDTO
import com.isp.restaurantapp.models.dto.FrbOrderDTO
import com.isp.restaurantapp.models.dto.GoodsItemDTO
import com.isp.restaurantapp.repositories.ICollectionGetterById
import com.isp.restaurantapp.repositories.RepositoryAbstract
import com.isp.restaurantapp.repositories.RepositoryRetrofit
import com.isp.restaurantapp.repositories.concrete.FrbOrdersInsertService
import com.isp.restaurantapp.repositories.concrete.FrbUserAllergensGetter
import com.isp.restaurantapp.repositories.interfaces.FrbDocumentsInsertService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MenuHolderVM(): ViewModel() {

    companion object{
        const val TAG = "MenuHolderVM"
    }

    private lateinit var job: Job


    private val data: RepositoryAbstract = RepositoryRetrofit()
    //private val data: RepositoryAbstract = RepositoryDataMock()

    private val _documentInserter: FrbDocumentsInsertService<FrbOrderDTO> by lazy {
        FrbOrdersInsertService()
    }

    // USER DEFINED ALLERGENS
    private val _userDefinedAllergensGetter: ICollectionGetterById<AllergenDTO, String> by lazy {
        FrbUserAllergensGetter()  //Repository of allergens
    }

    private val _userDefinedAllergens: MutableLiveData<MutableSet<AllergenDTO>> by lazy{
        MutableLiveData<MutableSet<AllergenDTO>>()
    }
    val userDefinedAllergens: LiveData<MutableSet<AllergenDTO>>
        get() = _userDefinedAllergens

    private val _userDefinedAllergensString: MutableLiveData<String> = MutableLiveData()
    /**
     * contains ids of all allergens that user defined
     * e.g. [List]Allergens(1,3,7) -> "137"
      */

    val userDefinedAllergensString: LiveData<String>
        get() = _userDefinedAllergensString



    private val _goodsItems = MutableLiveData<List<GoodsItemDTO>>()

    /**
     * observed by view
     *
     * Every [GoodsItemDTO] from [_goodsItems] are transformed (mapped) into [ItemCategory] that
     *  contains category:[String] and items:[List] that belongs to that category
     */
    val menuCategories: LiveData<List<ItemCategory>>
        get() = _goodsItems.map() { items ->
            items.groupBy {
                it.categoryId
            }.map{ categoriesMap ->
                ItemCategory(
                    categoriesMap.value.first().categoryName,
                    categoriesMap.value.distinctBy { it.goodsId }.sortedBy { containsAllergenCollision(it) }
                )
            }
        }

    /**
     * Cannot be observed in view.kt since there is none
     * It is not a good idea to put .observe() method to adapter
     *
     * so I created separate MLD that is filled during the fetch phase
     *
     * In view.xml it is implemented with text="@{viewmodel.goodsAllergens.get(item.goodId)}"
     */
    private val _goodsAllergens: MutableLiveData<Map<Int, String>> by lazy {
        MutableLiveData()
    }
    val goodsAllergens: LiveData<Map<Int, String>>
        get() = _goodsAllergens

    /**
     * Mediator observes LiveData by addSource
     * If the observed data changes its state, the [checkAllDataFetched] is called and the Mediator
     *  sets its value to the value it got from [checkAllDataFetched] method.
     *
     * If all data is fetched, Mediator sets its value to true and propagates this change
     *  to all his observers via exposed [isAllDataFetched]
     */
    private val _mediatorLiveDataCheck = MediatorLiveData<Boolean>().apply {
        addSource(menuCategories){
            value = checkAllDataFetched()
        }
        addSource(userDefinedAllergensString){
            value = checkAllDataFetched()
        }
        addSource(goodsAllergens){
            value = checkAllDataFetched()
        }
    }
    val isAllDataFetched: LiveData<Boolean> = _mediatorLiveDataCheck

    /**
     * Fetches [_goodsItems] from repository.
     * Then all allergens are transformed it into map [_goodsAllergens].
     *
     * After every fetch and transform operations are done, the results are published by a main
     *  thread
     */
    fun fetchData() {
        viewModelScope.launch(Dispatchers.IO) {
            // fetch from repository
            val result = data.getGoods()
            // map the allergens strings to belonging goodsId
            val allergensMap = result
                .groupBy { it.goodsId }
                .mapValues { (_, itemsWithSameId) -> itemsWithSameId.mapNotNull { it.allergenId }.joinToString() }

            // Publish results to Main thread
            withContext(Dispatchers.Main){
                _goodsItems.postValue(result)
                _goodsAllergens.postValue(allergensMap)
                Log.e(TAG, "Result from db fetch $result")
            }
        }
    }

    /**
     * If the user is not authenticated, pass empty [String]
     *
     * Method fetches and prepares user defined allergens, then it builds [userDefinedAllergensString]
     *  so the compare [containsAllergenCollision] method can easily check whether there is a
     *  allergen collision
     */
    fun fetchUserDefinedAllergens(uid: String){
        if (uid.isEmpty()){
            _userDefinedAllergens.postValue(mutableSetOf())
            _userDefinedAllergensString.postValue("")
            throw UserNotAuthenticatedException()
        }
        viewModelScope.launch(Dispatchers.IO){
            try {
                val result = _userDefinedAllergensGetter.getCollection(uid).toMutableSet()
                val stringMap: String = result.joinToString("") { it.id.toString() }

                withContext(Dispatchers.Main){
                    _userDefinedAllergens.postValue(result)
                    _userDefinedAllergensString.postValue(stringMap)
                    Log.i(TAG, "Loaded user defined allergens: ${_userDefinedAllergens.value.toString()}")
                }
            } catch (e: Exception){
                Log.e(TAG, "Error while getting user defined allergens", e)
                throw e
            }
        }
    }


    /**
     * Returns true if the given Allergen is contained in a prebuilt [userDefinedAllergensString]
     *  that contains all user defined allergen
     *
     * Can be used as a selector for [sortedBy] filter function
     */
    fun containsAllergenCollision(goodsItem: GoodsItemDTO): Boolean{
        var isCollision = false
        val containsAnyAllergen = _goodsAllergens.value?.containsKey(goodsItem.goodsId)
        if (containsAnyAllergen == true){
            val allergenString =_goodsAllergens.value?.get(goodsItem.goodsId).orEmpty()
            val allergenCharArray = _userDefinedAllergensString.value.orEmpty().toCharArray()
            //val allergenCharArray = userDefinedAllergensString.value?.toCharArray()?: charArrayOf()

            isCollision = allergenString.indexOfAny(allergenCharArray, ignoreCase = true) >= 0

            Log.i(TAG, "Item with id=${goodsItem.goodsId}" +
                    " contain allergens: $allergenString" +
                    " compares with: ${allergenCharArray.asList()} " +
                    " so the collision is $isCollision")
        }
        return isCollision
    }

    fun orderButtonClicked(goodsItem: GoodsItemDTO,
                           tableId: Int,
                           userId: String = "") {
        Log.w(TAG, "Buy item Id: " + goodsItem.goodsId.toString() +
                " " + goodsItem.goodsName +
                " for: " + goodsItem.price.toString() + "Kč."
        )

        viewModelScope.launch(Dispatchers.IO) {
            val insertedId = insertOrder(goodsItem, tableId, userId)
            val resultFrb = insertOrderToFirebase(goodsItem, tableId, insertedId, userId)
            withContext(Dispatchers.Main){
                when(resultFrb){
                    is Resource.Failure -> Log.e(TAG, "Something went wrong with order insertion: ${resultFrb.exception}")
                    is Resource.Success -> Log.i(TAG, "Order placed!")
                    else -> {}
                }
            }
        }
    }

    private suspend fun insertOrderToFirebase(
        goodsItem: GoodsItemDTO, tableId: Int, insertedId: Int, userId: String = ""
    ): Resource<Unit> {
        val mappedDTO = FrbOrderMapper.toFrbOrderDTO(goodsItem, tableId, insertedId, uid = userId)
        return _documentInserter.insertDocuments(listOf(mappedDTO))
    }

    private suspend fun insertOrder(
        goodsItem: GoodsItemDTO, tableId: Int, userId: String
    ): Int {
        val o = goodsItem
        return data.insertOrder(o.price.toDouble(), userId, o.goodsId, tableId).body()?.id ?: -1
    }

    /**
     * Checks whether [_userDefinedAllergensString] and [_goodsAllergens] are fetched
     *
     * there is no need to check anything else since the [_userDefinedAllergensString] is fetched
     *  via same method as [_userDefinedAllergens]
     *
     * since [_goodsAllergens] contains a map that was mapped with allergens from all
     *  items ([_goodsItems]), the same thing as above applies here
     */
    private fun checkAllDataFetched(): Boolean{
        return (_userDefinedAllergensString.value != null
                && _goodsAllergens.value != null)
    }

    override fun onCleared() {
        super.onCleared()
        if (::job.isInitialized) job.cancel()
    }
}