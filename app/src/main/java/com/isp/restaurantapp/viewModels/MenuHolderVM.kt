package com.isp.restaurantapp.viewModels

import android.app.Application
import android.nfc.Tag
import android.util.Log
import android.view.animation.Transformation
import android.widget.Toast
import androidx.lifecycle.*
import com.isp.restaurantapp.coroutines.Coroutines
import com.isp.restaurantapp.models.GoodsItem
import com.isp.restaurantapp.models.ItemCategory
import com.isp.restaurantapp.models.dto.GoodsItemDTO
import com.isp.restaurantapp.repositories.RepositoryAbstract
import com.isp.restaurantapp.repositories.RepositoryDataMock
import com.isp.restaurantapp.repositories.RepositoryRetrofit
import kotlinx.coroutines.Job
import kotlinx.coroutines.async

class MenuHolderVM(application: Application): AndroidViewModel(application) {

    companion object{
        const val TAG = "MenuHolderVM"
    }

    private lateinit var job: Job

    private val data: RepositoryAbstract = RepositoryRetrofit()
    //private val data: RepositoryAbstract = RepositoryDataMock()

    private val _goodsItems = MutableLiveData<List<GoodsItemDTO>>()
    val goodsItems: LiveData<List<GoodsItemDTO>> = _goodsItems
    
    //val isDatasetInitiated: LiveData<Boolean> = _goodsItems.map{ it.isNotEmpty() }


//    val menuCategories: LiveData<List<ItemCategory>> = _goodsItems.map() { items ->
//        items.groupBy { it.categoryId }
//            .map { ItemCategory(it.value.first().categoryName, it.value) }
//    }

    val menuCategories: LiveData<List<ItemCategory>> = _goodsItems.switchMap { items ->
        val deferred = viewModelScope.async {
            items.groupBy { it.categoryId }
                .map { ItemCategory(it.value.first().categoryName, it.value) }
        }
        liveData {
            emit(deferred.await())
        }
    }

//    private val _menuCategories = mutableListOf<ItemCategory>()
//    val menuCategories: LiveData<List<ItemCategory>> = MutableLiveData(_menuCategories)


    fun getCategories() {
        job = Coroutines.ioTheMain(
            { data.getGoods() },
            {
                _goodsItems.value = it
            }
        )
    }

//    fun makeCategories(){
//        _menuCategories.value = _goodsItems.map() { items ->
//        items.groupBy { it.categoryId }
//            .map { ItemCategory(it.value.first().categoryName, it.value) }
//    }
//    }
    

    fun orderButtonClicked(goodsItem: GoodsItemDTO) {
        Toast.makeText(getApplication(),
            "Buy item Id: " + goodsItem.goodsId.toString()
                    + " " + goodsItem.goodsName + " for: "
                    + goodsItem.price.toString() + " Kč.",
            Toast.LENGTH_LONG).show()
    }

    override fun onCleared() {
        super.onCleared()
        if (::job.isInitialized) job.cancel()
    }

    init {

    }

}