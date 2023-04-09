package com.isp.restaurantapp.repositories.concrete

import com.isp.restaurantapp.models.Allergen
import com.isp.restaurantapp.models.firebase.FrbFieldsAllergen
import com.isp.restaurantapp.models.firebase.FirestoreCollections
import com.isp.restaurantapp.repositories.ICollectionGetter
import com.isp.restaurantapp.repositories.MyFrb
import kotlinx.coroutines.tasks.await

class FrbAllergensGetter: ICollectionGetter<Allergen>, MyFrb() {
    override suspend fun getCollection(): List<Allergen> {
        val allergensRef = db.collection(FirestoreCollections.ALLERGENS)
        val snapshot = allergensRef.get().await()
        val allergens = snapshot.documents.map { document ->
            Allergen(
                id = document.getLong(FrbFieldsAllergen.ID)?.toInt() ?: 0,
                name = document.getString(FrbFieldsAllergen.NAME) ?: ""
            )
        }
        return allergens
    }
}
