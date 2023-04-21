package com.isp.restaurantapp.repositories.concrete

import android.util.Log
import com.google.firebase.firestore.Query.Direction
import com.google.firebase.firestore.ktx.toObjects
import com.isp.restaurantapp.models.dto.FrbOrderDTO
import com.isp.restaurantapp.models.firebase.FirestoreCollections
import com.isp.restaurantapp.models.firebase.FrbFieldsOrders
import com.isp.restaurantapp.repositories.MyFirestore
import com.isp.restaurantapp.repositories.interfaces.FrbRealtimeByStateGetterService
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class FrbRealtimeByStateGetterServiceImpl: FrbRealtimeByStateGetterService<FrbOrderDTO>, MyFirestore() {

    companion object {
        const val TAG = "Repository/FrbRealTimeOrderPaid"

    }
    override fun getItemsRealtime(
        byState: FrbFieldsOrders.States,
        limit: Long,
        orderDirection: Direction): Flow<List<FrbOrderDTO>> = callbackFlow {

        val itemsCollection = firestore.collection(FirestoreCollections.ORDERS)
            .whereEqualTo(FrbFieldsOrders.FIELD_PAYMENT_STATE, byState.value)
        //itemsCollection
        //    .orderBy(FrbFieldsOrders.FIELD_LAST_UPDATE, orderDirection)
            .limit(limit)
        val listenerRegistration = itemsCollection.addSnapshotListener { snapshot, exception ->
            exception?.let {
                Log.e(TAG, "Listen error: ${exception.toString()}")
                return@addSnapshotListener
            }

            snapshot?.let {
                Log.i(TAG, "Producer tries to publish pending orders")
                val orders = it.toObjects<FrbOrderDTO>()
                trySend(orders).isSuccess
            }
        }

        awaitClose {
            listenerRegistration.remove()
        }
    }
}