package com.isp.restaurantapp.repositories.interfaces

import kotlinx.coroutines.flow.Flow

interface FrbRealtimeGetterService<T> {
    fun getItemsRealtime(uid: String): Flow<List<T>>
}