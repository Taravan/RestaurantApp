package com.isp.restaurantapp.repositories.concrete

import com.isp.restaurantapp.models.dto.AllergenDTO
import com.isp.restaurantapp.models.exceptions.DocumentNotFoundException
import com.isp.restaurantapp.models.firebase.FirestoreCollections
import com.isp.restaurantapp.models.firebase.FrbFieldsUsersAllergen
import com.isp.restaurantapp.repositories.ICollectionGetterById
import com.isp.restaurantapp.repositories.MyFrb
import kotlinx.coroutines.tasks.await

class FrbUserAllergensGetter
    : ICollectionGetterById<AllergenDTO, String>, MyFrb() {
    override suspend fun getCollection(id: String): List<AllergenDTO> {
        val allergenList = mutableListOf<AllergenDTO>()
        val userAllergensCollection = db.collection(FirestoreCollections.USER_ALLERGENS)
        val userAllergensDocument = userAllergensCollection.document(id).get().await()

        if (userAllergensDocument.exists()) {
            val allergensArray = userAllergensDocument
                .get(FrbFieldsUsersAllergen.ALLERGENS) as? ArrayList<*> ?: ArrayList<String>()

            // if there is for some reason empty response, return empty
            // the reason- can happen that update fails and in db there is no array of maps
            // even tho the uid document exist (but totally empty due to update fail)
            if (allergensArray.isEmpty())
                return allergenList


            for (allergen in allergensArray) {
                val allergenMap = allergen as HashMap<*, *>
                val allergenId = allergenMap[FrbFieldsUsersAllergen.Allergens.ID] as Long
                val allergenName = allergenMap[FrbFieldsUsersAllergen.Allergens.NAME] as String
                allergenList.add(AllergenDTO(allergenId.toInt(), allergenName))
            }
        }
        else{
            throw DocumentNotFoundException()
        }
        return allergenList
    }
}
