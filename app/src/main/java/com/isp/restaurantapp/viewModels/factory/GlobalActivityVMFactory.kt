package com.isp.restaurantapp.viewModels.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.isp.restaurantapp.viewModels.CustomerActivityVM

@Suppress("UNCHECKED_CAST")
class GlobalActivityVMFactory(private val activityViewModel: CustomerActivityVM): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return  modelClass.constructors[0].newInstance(activityViewModel) as T
    }

}