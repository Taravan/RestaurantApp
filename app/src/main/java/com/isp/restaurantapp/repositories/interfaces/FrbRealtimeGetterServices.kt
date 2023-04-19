package com.isp.restaurantapp.repositories.interfaces

import com.google.firebase.firestore.Query
import com.google.firebase.firestore.Query.Direction
import com.isp.restaurantapp.models.firebase.FrbFieldsOrders
import kotlinx.coroutines.flow.Flow

interface FrbRealtimeGetterByUidService<T> {
    fun getItemsRealtime(uid: String = ""): Flow<List<T>>
}
interface FrbRealtimeGetterByTableIdService<T> {
    fun getItemsRealtime(tableId: Int): Flow<List<T>>
}
interface FrbRealtimeGetterByTableIdAndStateService<T> {
    fun getItemsRealtime(
        tableId: Int,
        byState: FrbFieldsOrders.States,
        limit: Long = 1000,
        orderDirection: Direction = Direction.ASCENDING): Flow<List<T>>
}

interface FrbRealtimeByStateGetterService<T>{
    fun getItemsRealtime(
        byState: FrbFieldsOrders.States,
        limit: Long = 1000,
        orderDirection: Direction = Direction.ASCENDING): Flow<List<T>>
}