package com.isp.restaurantapp.repositories.concrete

import android.util.Log
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObjects
import com.isp.restaurantapp.models.dto.FrbOrderDTO
import com.isp.restaurantapp.models.firebase.FirestoreCollections
import com.isp.restaurantapp.models.firebase.FrbFieldsOrders
import com.isp.restaurantapp.repositories.MyFirestore
import com.isp.restaurantapp.repositories.interfaces.FrbRealtimeGetterByTableIdAndStateService
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class FrbRealtimeGetterByTableIdAndStateServiceImpl :
    FrbRealtimeGetterByTableIdAndStateService<FrbOrderDTO> {
    companion object {
        const val TAG = "Repository/FrbRealtimeGetterByTableIdAndStateServiceImpl"

    }
    override fun getItemsRealtime(
        tableId: Int,
        byState: FrbFieldsOrders.States,
        limit: Long,
        orderDirection: Query.Direction
    ): Flow<List<FrbOrderDTO>> = callbackFlow {

        val itemsCollection = MyFirestore.firestore.collection(FirestoreCollections.ORDERS)
            .whereEqualTo(FrbFieldsOrders.FIELD_PAYMENT_STATE, byState.value)
            .whereEqualTo(FrbFieldsOrders.FIELD_TABLE_ID, tableId)
            //itemsCollection
            //    .orderBy(FrbFieldsOrders.FIELD_LAST_UPDATE, orderDirection)
            .limit(limit)
        val listenerRegistration = itemsCollection.addSnapshotListener { snapshot, exception ->
            exception?.let {
                Log.e(TAG, "Listen error: $exception")
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