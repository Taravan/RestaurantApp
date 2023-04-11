package com.isp.restaurantapp.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.isp.restaurantapp.models.Table

class LocalRepo private constructor(){
    private lateinit var table: Table

    fun setTable(table: Table) {
        this.table = table
    }

    fun getTable(): Table {
        return table
    }

    companion object {
        @Volatile
        private var instance: LocalRepo? = null

        fun getInstance(): LocalRepo {
            return instance ?: synchronized(this) {
                instance ?: LocalRepo().also { instance = it }
            }
        }
    }
}