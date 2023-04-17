package com.isp.restaurantapp.repositories.concrete

import android.util.Log
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.ktx.toObjects
import com.isp.restaurantapp.models.dto.FrbOrderDTO
import com.isp.restaurantapp.models.firebase.FirestoreCollections
import com.isp.restaurantapp.models.firebase.FrbFieldsOrders
import com.isp.restaurantapp.repositories.MyFirestore
import com.isp.restaurantapp.repositories.interfaces.FrbRealtimeGetterByTableIdService
import com.isp.restaurantapp.repositories.interfaces.FrbRealtimeGetterByUidService
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class FrbRealtimeOrder: FrbRealtimeGetterByTableIdService<FrbOrderDTO>, MyFirestore() {

    companion object {
        const val TAG = "Repository/FrbRealTimeOrderPaid"

    }
    override fun getItemsRealtime(tableId: Int): Flow<List<FrbOrderDTO>> = callbackFlow {
        val itemsCollection = firestore.collection(FirestoreCollections.ORDERS)
            .whereEqualTo(FrbFieldsOrders.FIELD_TABLE_ID, tableId)
            .where(Filter.or(
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