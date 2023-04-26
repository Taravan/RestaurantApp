package com.isp.restaurantapp.repositories.concrete

import android.util.Log
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObjects
import com.isp.restaurantapp.models.dto.FrbOrderDTO
import com.isp.restaurantapp.models.firebase.FirestoreCollections
import com.isp.restaurantapp.models.firebase.FrbFieldsOrders
import com.isp.restaurantapp.repositories.MyFirestore
import com.isp.restaurantapp.repositories.interfaces.FrbRealtimeGetterService
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class FrbRealtimeOrderABC: FrbRealtimeGetterService<FrbOrderDTO> {
    companion object {
        const val TAG = "Repository/FrbRealTimeOrderABC"

    }

    override fun getItemsRealtime(
        limit: Long,
        orderDirection: Query.Direction
    ): Flow<List<FrbOrderDTO>> = callbackFlow  {
        val itemsCollection = MyFirestore.firestore.collection(FirestoreCollections.ORDERS)
            .where(
                Filter.or(
                    Filter.equalTo(FrbFieldsOrders.FIELD_PAYMENT_STATE, FrbFieldsOrders.States.PENDING.value),
                    Filter.equalTo(FrbFieldsOrders.FIELD_PAYMENT_STATE, FrbFieldsOrders.States.CONFIRMED.value),
                    Filter.equalTo(FrbFieldsOrders.FIELD_PAYMENT_STATE, FrbFieldsOrders.States.FOR_PAYMENT.value)
                ))
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