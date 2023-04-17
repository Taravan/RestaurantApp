package com.isp.restaurantapp.repositories.interfaces

import com.isp.restaurantapp.models.Resource
import com.isp.restaurantapp.models.firebase.FrbFieldsOrders

interface FrbDocumentStateUpdaterService<T> {
    suspend fun updateDocuments(
        documents: List<T>, toState: FrbFieldsOrders.States, uid:String = ""
    ): Resource<Unit>
}