package com.isp.restaurantapp.repositories.interfaces

import kotlinx.coroutines.flow.Flow

interface FrbRealtimeGetterService<T> {
    fun getItems(uid: String): Flow<List<T>>
}