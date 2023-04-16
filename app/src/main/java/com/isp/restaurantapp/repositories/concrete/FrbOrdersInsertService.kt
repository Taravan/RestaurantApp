package com.isp.restaurantapp.repositories.concrete

import com.isp.restaurantapp.models.FrbOrderMapper
import com.isp.restaurantapp.models.Resource
import com.isp.restaurantapp.models.dto.FrbOrderDTO
import com.isp.restaurantapp.models.firebase.FirestoreCollections
import com.isp.restaurantapp.repositories.MyFirestore
import com.isp.restaurantapp.repositories.interfaces.FrbDocumentsInsertService
import kotlinx.coroutines.tasks.await

class FrbOrdersInsertService: FrbDocumentsInsertService<FrbOrderDTO>, MyFirestore() {
    override suspend fun insertDocuments(
        documents: List<FrbOrderDTO>,uid: String
    ): Resource<Unit> {
        return try {
            for (order in documents) {
                val docRef = firestore.collection(FirestoreCollections.ORDERS).document()
                val orderMap = FrbOrderMapper.toFrbOrder(order)
                if (uid.isNotEmpty()) order.uid = uid
                docRef.set(orderMap).await()
            }
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Failure(e)
        }
    }

}