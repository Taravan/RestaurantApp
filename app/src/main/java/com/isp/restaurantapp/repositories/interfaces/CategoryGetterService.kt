package com.isp.restaurantapp.repositories.interfaces

import com.isp.restaurantapp.BuildConfig
import com.isp.restaurantapp.models.dto.CategoryDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

interface CategoryGetterService {
    @Headers(BuildConfig.API_KEY_HEADER)
    @GET(BuildConfig.SELECT_GOODS_CATEGORIES)
    suspend fun getCategories(): Response<List<CategoryDTO>>
}