package com.isp.restaurantapp.repositories.interfaces

import kotlinx.coroutines.flow.Flow

interface FrbRealtimeGetterByUidService<T> {
    fun getItemsRealtime(uid: String = ""): Flow<List<T>>
}
interface FrbRealtimeGetterByTableIdService<T> {
    fun getItemsRealtime(tableId: Int): Flow<List<T>>
}