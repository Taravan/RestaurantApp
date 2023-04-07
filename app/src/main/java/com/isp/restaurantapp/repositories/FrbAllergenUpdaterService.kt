package com.isp.restaurantapp.repositories

import android.util.Log
import com.google.firebase.firestore.FieldValue
import com.isp.restaurantapp.models.Allergen
import com.isp.restaurantapp.models.firebase.FrbFieldsAllergen
import com.isp.restaurantapp.models.firebase.FirestoreCollections
import com.isp.restaurantapp.models.firebase.FrbFieldsUsersAllergen
import kotlinx.coroutines.tasks.await

class FrbAllergenUpdaterService(): MyFrb(), IAllergenUpdaterService {
    override suspend fun updateAllergens(uid: String, allergenList: List<Allergen>) {
        val allergenArray = mutableListOf<Map<String, Any>>()

        allergenList.forEach { allergen ->
            allergenArray.add(mapOf(FrbFieldsAllergen.ID to allergen.id, FrbFieldsAllergen.NAME to allergen.name))
        }

        val userAllergensDocRef = db.collection(FirestoreCollections.USER_ALLERGENS).document(uid)

        // Populate the new allergen array with the data you want to set in Firestore
        // ...

        // Delete the existing allergen array field and set the new allergen array field
        userAllergensDocRef.update(
            FrbFieldsUsersAllergen.ALLERGENS, FieldValue.delete()
        ).addOnSuccessListener {
            Log.e("FrbUpdater", "Field Deleted Successfully, initiating update process.")
            userAllergensDocRef.update(FrbFieldsUsersAllergen.ALLERGENS, allergenArray)
        }.await()
    }
}