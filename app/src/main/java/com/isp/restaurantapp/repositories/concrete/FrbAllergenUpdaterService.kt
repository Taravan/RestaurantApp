package com.isp.restaurantapp.repositories.concrete

import android.util.Log
import com.google.firebase.firestore.FieldValue
import com.isp.restaurantapp.models.dto.AllergenDTO
import com.isp.restaurantapp.models.firebase.FrbFieldsAllergen
import com.isp.restaurantapp.models.firebase.FirestoreCollections
import com.isp.restaurantapp.models.firebase.FrbFieldsUsersAllergen
import com.isp.restaurantapp.repositories.MyFirestore
import com.isp.restaurantapp.repositories.interfaces.IAllergenUpdaterService
import kotlinx.coroutines.tasks.await

class FrbAllergenUpdaterService(): MyFirestore(), IAllergenUpdaterService {
    override suspend fun updateAllergens(uid: String, allergenList: List<AllergenDTO>) {
        val allergenArray = mutableListOf<Map<String, Any>>()

        allergenList.forEach { allergen ->
            allergenArray.add(mapOf(FrbFieldsAllergen.ID to allergen.id, FrbFieldsAllergen.NAME to allergen.name))
        }

        val userAllergensDocRef = firestore.collection(FirestoreCollections.USER_ALLERGENS).document(uid)

        // Populate the new allergen array with the data you want to set in Firestore
        // ...

        // Delete the existing allergen array field and set the new allergen array field
        userAllergensDocRef.update(
            FrbFieldsUsersAllergen.ALLERGENS, FieldValue.delete()
        ).addOnSuccessListener {
            Log.i("FrbUpdater", "Field Deleted Successfully, initiating update process.")
            userAllergensDocRef.update(FrbFieldsUsersAllergen.ALLERGENS, allergenArray)
        }.await()
    }
}