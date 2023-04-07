package com.isp.restaurantapp.repositories

interface ICollectionGetter<T>{
    suspend fun getCollection(): List<T>
}
interface ICollectionGetterById<T, S>{
    suspend fun getCollection(id: S): List<T>
}