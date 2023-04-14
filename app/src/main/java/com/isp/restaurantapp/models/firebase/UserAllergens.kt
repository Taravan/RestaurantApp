package com.isp.restaurantapp.models.firebase

import com.isp.restaurantapp.models.dto.AllergenDTO

data class UserAllergens (
    val userId: String,
    val allergens: List<AllergenDTO>
    )