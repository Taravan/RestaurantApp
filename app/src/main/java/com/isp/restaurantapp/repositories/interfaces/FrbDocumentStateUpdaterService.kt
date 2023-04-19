package com.isp.restaurantapp.repositories.interfaces

import com.isp.restaurantapp.models.Resource

interface FrbDocumentStateUpdaterService<T> {
    suspend fun updateDocuments(
        documents: List<T>
    ): Resource<Unit>
}