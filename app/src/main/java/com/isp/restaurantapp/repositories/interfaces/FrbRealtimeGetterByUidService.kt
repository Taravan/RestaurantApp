package com.isp.restaurantapp.repositories.interfaces

import com.google.firebase.firestore.Query
import com.isp.restaurantapp.models.firebase.FrbFieldsOrders
import kotlinx.coroutines.flow.Flow

interface FrbRealtimeGetterByUidService<T> {
    fun getItemsRealtime(uid: String = ""): Flow<List<T>>
}
interface FrbRealtimeGetterByTableIdService<T> {
    fun getItemsRealtime(tableId: Int): Flow<List<T>>
}
interface FrbRealtimeByStateGetterService<T>{
    fun getItemsRealtime(
        newState: FrbFieldsOrders.States,
        limit: Long = 1000,
        orderDirection: Query.Direction = Query.Direction.ASCENDING): Flow<List<T>>
}