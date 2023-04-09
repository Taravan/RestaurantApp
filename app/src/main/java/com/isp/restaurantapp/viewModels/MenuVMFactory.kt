package com.isp.restaurantapp.viewModels

import androidx.lifecycle.ViewModelProvider
import com.isp.restaurantapp.repositories.RepositoryDataMock

@Suppress("UNCHECKED_CAST")
class MenuVMFactory(private val data: RepositoryDataMock): ViewModelProvider.NewInstanceFactory() {

//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        return MenuHolderVM(data) as T
//    }

}