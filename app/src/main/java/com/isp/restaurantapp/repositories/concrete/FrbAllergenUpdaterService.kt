package com.isp.restaurantapp.repositories.concrete

import android.util.Log
import com.google.firebase.firestore.FieldValue
import com.isp.restaurantapp.models.dto.AllergenDTO
import com.isp.restaurantapp.models.firebase.FrbFieldsAllergen
import com.isp.restaurantapp.models.firebase.FirestoreCollections
import com.isp.restaurantapp.models.firebase.FrbFieldsUsersAllergen
import com.isp.restaurantapp.repositories.MyFirestore
import com.isp.restaurantapp.repositories.interfaces.IAllergenUpdaterService

class FrbAllergenUpdaterService(): MyFirestore(), IAllergenUpdaterService {
    companion object{
        private const val TAG = "FrbAllergenUpdaterService"
    }
    override suspend fun updateAllergens(uid: String, allergenList: List<AllergenDTO>) {

        val allergenArray = mutableListOf<Map<String, Any>>()
        allergenList.forEach { allergen ->
            allergenArray.add(mapOf(
                FrbFieldsAllergen.ID to allergen.id,
                FrbFieldsAllergen.NAME to allergen.name))
        }

        val userAllergensDocRef = firestore.collection(FirestoreCollections.USER_ALLERGENS).document(uid)
        userAllergensDocRef.get().addOnCompleteListener{ task ->
            if (task.isSuccessful){
                val document = task.result
                if (document.exists()){
                    userAllergensDocRef.update(
                        FrbFieldsUsersAllergen.FIELD_ALLERGENS, FieldValue.delete()
                    ).addOnSuccessListener {
                        Log.i("FrbUpdater", "Field Deleted Successfully, initiating update process.")
                        userAllergensDocRef.update(FrbFieldsUsersAllergen.FIELD_ALLERGENS, allergenArray)
                    }
                }
                else{
                    userAllergensDocRef.set(mapOf(FrbFieldsUsersAllergen.FIELD_ALLERGENS to allergenArray))
                        .addOnSuccessListener{
                        Log.i(TAG, "Document created and fields with allergens has been set")
                    }.addOnFailureListener {
                        Log.e(TAG, "Document could not be created! Error: ${it.message}")
                    }
                }
            }
            else {
                userAllergensDocRef.set(allergenList)
            }
        }
    }
}