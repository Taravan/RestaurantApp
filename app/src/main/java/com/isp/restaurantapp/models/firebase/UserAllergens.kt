package com.isp.restaurantapp.models.firebase

import com.isp.restaurantapp.models.Allergen

data class UserAllergens (
    val userId: String,
    val allergens: List<Allergen>
    )