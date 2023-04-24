package com.isp.restaurantapp.repositories.interfaces

import com.isp.restaurantapp.BuildConfig
import com.isp.restaurantapp.models.dto.AllergenDTO
import com.isp.restaurantapp.models.dto.CategoryDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

interface AllergenGetterService {
    @Headers(BuildConfig.API_KEY_HEADER)
    @GET(BuildConfig.SELECT_ALLERGENS)
    suspend fun getAllergens(): Response<List<AllergenDTO>>
}