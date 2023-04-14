package com.isp.restaurantapp.viewModels

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.isp.restaurantapp.models.ItemCategory
import com.isp.restaurantapp.models.dto.GoodsItemDTO
import com.isp.restaurantapp.repositories.RepositoryAbstract
import com.isp.restaurantapp.repositories.RepositoryRetrofit
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

    private val _goodsItems = MutableLiveData<List<GoodsItemDTO>>()

    /**
     * observed by view.kt
     */
    val menuCategories: LiveData<List<ItemCategory>>
        get() = _goodsItems.map() { items ->
            items.groupBy {
                it.categoryId
            }.map{
                ItemCategory(it.value.first().categoryName, it.value.distinctBy { it.goodsId })
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


    fun orderButtonClicked(goodsItem: GoodsItemDTO) {
        Log.w(TAG, "Buy item Id: " + goodsItem.goodsId.toString() + " " + goodsItem.goodsName +
                        " for: " + goodsItem.price.toString() + "Kč.")
    }

    override fun onCleared() {
        super.onCleared()
        if (::job.isInitialized) job.cancel()
    }
}