package com.isp.restaurantapp.repositories.concrete

import android.util.Log

import com.isp.restaurantapp.models.dto.FrbOrderDTO
import com.isp.restaurantapp.repositories.MyFirestore
import com.isp.restaurantapp.repositories.interfaces.FrbRealtimeGetterService

import com.google.firebase.firestore.ktx.toObjects
import com.isp.restaurantapp.models.firebase.FirestoreCollections
import com.isp.restaurantapp.models.firebase.FrbFieldsOrders
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

class FrbRealTimeOrder: FrbRealtimeGetterService<FrbOrderDTO>, MyFirestore() {

    companion object {
        const val TAG = "Repository/FrbRealTimeOrder"

    }
    override fun getItemsRealtime(uid: String): Flow<List<FrbOrderDTO>> = callbackFlow {
        val itemsCollection = firestore.collection(FirestoreCollections.ORDERS)
            .whereEqualTo(
                FrbFieldsOrders.FIELD_PAYMENT_STATE, FrbFieldsOrders.States.PAID.value
            ).whereEqualTo(
                FrbFieldsOrders.FIELD_UID, uid
            )
        val listenerRegistration = itemsCollection.addSnapshotListener { snapshot, exception ->
            exception?.let {
                Log.e(TAG, "Listen error: ${exception.toString()}")
                return@addSnapshotListener
            }

            snapshot?.let {
                Log.i(TAG, "Producer tries to publish orders")
                val orders = it.toObjects<FrbOrderDTO>()
                trySend(orders).isSuccess
            }
        }

        awaitClose {
            listenerRegistration.remove()
        }
    }


}