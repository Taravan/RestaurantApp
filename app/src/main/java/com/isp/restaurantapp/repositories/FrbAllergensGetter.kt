package com.isp.restaurantapp.repositories

import com.isp.restaurantapp.models.Allergen
import com.isp.restaurantapp.models.firebase.AllergenFields
import com.isp.restaurantapp.models.firebase.FirestoreCollections
import kotlinx.coroutines.tasks.await

class FrbAllergensGetter: ICollectionGetter<Allergen>, MyFrb() {
    override suspend fun getCollection(): List<Allergen> {
        val allergensRef = db.collection(FirestoreCollections.ALLERGENS)
        val snapshot = allergensRef.get().await()
        val allergens = snapshot.documents.map { document ->
            Allergen(
                id = document.getLong(AllergenFields.ID)?.toInt() ?: 0,
                name = document.getString(AllergenFields.NAME) ?: ""
            )
        }
        return allergens
    }
}
