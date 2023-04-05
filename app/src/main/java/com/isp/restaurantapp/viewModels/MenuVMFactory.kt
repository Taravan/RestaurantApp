package com.isp.restaurantapp.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.isp.restaurantapp.repositories.dataMock

@Suppress("UNCHECKED_CAST")
class MenuVMFactory(private val data: dataMock): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MenuVM(data) as T
    }

}