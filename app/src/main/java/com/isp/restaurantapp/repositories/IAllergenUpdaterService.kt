package com.isp.restaurantapp.repositories

import com.isp.restaurantapp.models.Allergen

interface IAllergenUpdaterService {
    suspend fun updateAllergens(uid:String, allergenList: List<Allergen>)
}