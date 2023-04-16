package com.isp.restaurantapp.repositories.concrete

import com.isp.restaurantapp.models.dto.AllergenDTO
import com.isp.restaurantapp.models.firebase.FrbFieldsAllergen
import com.isp.restaurantapp.models.firebase.FirestoreCollections
import com.isp.restaurantapp.repositories.ICollectionGetter
import com.isp.restaurantapp.repositories.MyFirestore
import kotlinx.coroutines.tasks.await

class FrbAllergensGetter: ICollectionGetter<AllergenDTO>, MyFirestore() {
    override suspend fun getCollection(): List<AllergenDTO> {
        val allergensRef = firestore.collection(FirestoreCollections.ALLERGENS).orderBy(FrbFieldsAllergen.ID)
        val snapshot = allergensRef.get().await()
        val allergens = snapshot.documents.map { document ->
            AllergenDTO(
                id = document.getLong(FrbFieldsAllergen.ID)?.toInt() ?: 0,
                name = document.getString(FrbFieldsAllergen.NAME) ?: ""
            )
        }
        return allergens
    }
}
