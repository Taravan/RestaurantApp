package com.isp.restaurantapp.repositories.interfaces

import com.isp.restaurantapp.models.Resource

interface FrbDocumentDeleter <T> {
    suspend fun deleteDocument(document: T): Resource<Unit>
}