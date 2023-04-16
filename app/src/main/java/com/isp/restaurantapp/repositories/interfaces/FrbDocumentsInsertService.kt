package com.isp.restaurantapp.repositories.interfaces

import com.isp.restaurantapp.models.Resource


interface FrbDocumentsInsertService<T> {
    suspend fun insertDocuments(documents: List<T>, uid:String = "" ): Resource<Unit>
}