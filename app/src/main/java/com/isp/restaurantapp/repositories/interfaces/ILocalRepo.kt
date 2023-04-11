package com.isp.restaurantapp.repositories.interfaces

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.isp.restaurantapp.models.Table

interface ILocalRepo {

    fun setTable(table: Table)
    fun getTable(): Table

}