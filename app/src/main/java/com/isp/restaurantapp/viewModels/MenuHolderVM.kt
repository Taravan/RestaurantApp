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

    val menuCategories: LiveData<List<ItemCategory>>
        get() = _goodsItems.map() { items ->
            items.groupBy { it.categoryId }.map{
                ItemCategory(it.value.first().categoryName, it.value)
            }
        }

    fun fetchData() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = data.getGoods()

            withContext(Dispatchers.Main){
                _goodsItems.postValue(result)
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