package com.isp.restaurantapp.repositories

import com.isp.restaurantapp.models.Allergen
import com.isp.restaurantapp.models.exceptions.DocumentNotFoundException
import com.isp.restaurantapp.models.firebase.FirestoreCollections
import com.isp.restaurantapp.models.firebase.UsersAllergenFields
import kotlinx.coroutines.tasks.await

class FrbUserAllergensGetter
    : ICollectionGetterById<Allergen, String>, MyFrb() {
    override suspend fun getCollection(id: String): List<Allergen> {
        val allergenList = mutableListOf<Allergen>()
        val userAllergensCollection = db.collection(FirestoreCollections.USER_ALLERGENS)
        val userAllergensDocument = userAllergensCollection.document(id).get().await()

        if (userAllergensDocument.exists()) {
            val allergensArray = userAllergensDocument
                .get(UsersAllergenFields.ALLERGENS) as? ArrayList<*> ?: ArrayList<String>()

            // if there is for some reason empty response, return empty
            // the reason- can happen that update fails and in db there is no array of maps
            // even tho the uid document exist (but totally empty due to update fail)
            if (allergensArray.isEmpty())
                return allergenList


            for (allergen in allergensArray) {
                val allergenMap = allergen as HashMap<*, *>
                val allergenId = allergenMap[UsersAllergenFields.Allergens.ID] as Long
                val allergenName = allergenMap[UsersAllergenFields.Allergens.NAME] as String
                allergenList.add(Allergen(allergenId.toInt(), allergenName))
            }
        }
        else{
            throw DocumentNotFoundException()
        }
        return allergenList
    }
}
