package com.isp.restaurantapp.repositories.concrete

import android.util.Log

import com.isp.restaurantapp.models.dto.FrbOrderDTO
import com.isp.restaurantapp.repositories.MyFirestore
import com.isp.restaurantapp.repositories.interfaces.FrbRealtimeGetterService

import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

class FrbRealTimeOrder: FrbRealtimeGetterService<FrbOrderDTO>, MyFirestore() {

    companion object {
        const val TAG = "Repository/FrbRealTimeOrder"

        const val ORDERS_COLLECTION_NAME = "orders"

        const val FIELD_ITEM_ID= "itemId"
        const val FIELD_ITEM_NAME = "itemName"
        const val FIELD_PRICE = "price"
        const val FIELD_TABLE_ID = "tableId"
        const val FIELD_PAYMENT_STATE = "state"
        const val FIELD_RECEIPT_ID = "receiptId"
        const val FIELD_UID = "uid"
        const val FIELD_LAST_UPDATE = "lastUpdate"

        const val STATE_PAID = "paid"
        const val STATE_PENDING = "pending"
        const val STATE_CONFIRMED = "confirmed"
    }



    override fun getItems(uid: String): Flow<List<FrbOrderDTO>> = callbackFlow {
        val itemsCollection = firestore.collection(ORDERS_COLLECTION_NAME)
            .whereEqualTo(FIELD_PAYMENT_STATE, STATE_PAID)
            .whereEqualTo(FIELD_UID, uid)
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