package com.isp.restaurantapp.viewModels

import androidx.lifecycle.ViewModelProvider
import com.isp.restaurantapp.repositories.DataMock

@Suppress("UNCHECKED_CAST")
class MenuVMFactory(private val data: DataMock): ViewModelProvider.NewInstanceFactory() {

//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        return MenuHolderVM(data) as T
//    }

}