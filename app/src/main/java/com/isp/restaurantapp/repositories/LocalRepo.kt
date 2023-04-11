package com.isp.restaurantapp.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.isp.restaurantapp.models.Table
import com.isp.restaurantapp.repositories.interfaces.ILocalRepo

class LocalRepo: ILocalRepo {

    private lateinit var table:Table

    override fun setTable(table: Table) {
        this.table = table
    }

    override fun getTable(): Table {
        return table
    }


}