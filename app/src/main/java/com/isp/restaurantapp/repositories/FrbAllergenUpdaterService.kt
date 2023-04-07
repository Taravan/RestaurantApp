package com.isp.restaurantapp.repositories

import android.util.Log
import com.google.firebase.firestore.FieldValue
import com.isp.restaurantapp.models.Allergen
import com.isp.restaurantapp.models.firebase.AllergenFields
import com.isp.restaurantapp.models.firebase.FirestoreCollections
import com.isp.restaurantapp.models.firebase.UsersAllergenFields
import com.google.firebase.firestore.ktx.getField
import kotlinx.coroutines.tasks.await

class FrbAllergenUpdaterService(): MyFrb(), IAllergenUpdaterService {
    override suspend fun updateAllergens(uid: String, allergenList: List<Allergen>) {
        val allergenArray = mutableListOf<Map<String, Any>>()

        allergenList.forEach { allergen ->
            allergenArray.add(mapOf(AllergenFields.ID to allergen.id, AllergenFields.NAME to allergen.name))
        }

        val userAllergensDocRef = db.collection(FirestoreCollections.USER_ALLERGENS).document(uid)

        // Populate the new allergen array with the data you want to set in Firestore
        // ...

        // Delete the existing allergen array field and set the new allergen array field
        userAllergensDocRef.update(
            UsersAllergenFields.ALLERGENS, FieldValue.delete()
        ).addOnSuccessListener {
            Log.e("FrbUpdater", "Field Deleted Successfully, initiating update process.")
            userAllergensDocRef.update(UsersAllergenFields.ALLERGENS, allergenArray)
        }.await()
    }
}