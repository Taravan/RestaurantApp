package com.isp.restaurantapp.repositories.concrete

import android.util.Log
import com.isp.restaurantapp.models.Resource
import com.isp.restaurantapp.models.dto.FrbOrderDTO
import com.isp.restaurantapp.models.firebase.FirestoreCollections
import com.isp.restaurantapp.repositories.MyFirestore
import com.isp.restaurantapp.repositories.interfaces.FrbDocumentDeleter

class FrbOrderDeleter: FrbDocumentDeleter<FrbOrderDTO>, MyFirestore() {
    companion object{
        private const val TAG = "FrbOrderDeleter"
    }
    override suspend fun deleteDocument(document: FrbOrderDTO): Resource<Unit> {
        return try {
            val docRef = firestore.collection(FirestoreCollections.ORDERS)
                .document(document.id.toString())
                .delete()
            Resource.Success(Unit)
        } catch (e: Exception){
            Resource.Failure(e)
        }
    }
}