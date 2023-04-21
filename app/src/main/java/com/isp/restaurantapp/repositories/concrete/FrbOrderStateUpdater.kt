package com.isp.restaurantapp.repositories.concrete

import android.util.Log
import com.isp.restaurantapp.models.Resource
import com.isp.restaurantapp.models.dto.FrbOrderDTO
import com.isp.restaurantapp.models.firebase.FirestoreCollections
import com.isp.restaurantapp.repositories.MyFirestore.Companion.firestore
import com.isp.restaurantapp.repositories.interfaces.FrbDocumentStateUpdaterService
import kotlinx.coroutines.tasks.await

class FrbOrderStateUpdater: FrbDocumentStateUpdaterService<FrbOrderDTO> {
    companion object{
        const val TAG = "FrbOrderStateUpdater"
    }

    override suspend fun updateDocuments(documents: List<FrbOrderDTO>): Resource<Unit> {
        Log.i(TAG, "Initiating state update for uid= ${documents.first().uid}")

        val batch = firestore.batch()

        // changes needs to happen on separate List so it doesn't cause concurrent error!
        val documentsCopy = documents.toList()
        for (order in documentsCopy) {
            /**
             * If the document still exists, update its state
             */
            val orderRef = order.id?.let {
                firestore.collection(FirestoreCollections.ORDERS).document(it)
            }
            if (orderRef != null) {
//                batch.update(orderRef,
//                    FrbFieldsOrders.FIELD_PAYMENT_STATE, toState.value
//                )
//                batch.update(orderRef,
//                    FrbFieldsOrders.FIELD_LAST_UPDATE, Timestamp.now()
//                )
//                if (uid.isNotEmpty()){
//                    batch.update(orderRef, FrbFieldsOrders.FIELD_UID, uid)
//                    Log.i(TAG, "uid updated with $uid")
//                }
                batch.set(orderRef, order)

            } else{
                Log.e("OrderStateUpdater", "Document id does not exist")
            }
        }

        return try {
            batch.commit().await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Failure(e)
        }
    }
}