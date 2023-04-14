package com.isp.restaurantapp.repositories

import com.isp.restaurantapp.models.dto.TableDTO

class LocalRepo private constructor(){
    private lateinit var table: TableDTO

    fun setTable(table: TableDTO) {
        this.table = table
    }

    fun getTable(): TableDTO {
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