package com.isp.restaurantapp.repositories.interfaces

import com.isp.restaurantapp.models.dto.AllergenDTO

interface IAllergenUpdaterService {
    suspend fun updateAllergens(uid:String, allergenList: List<AllergenDTO>)
}