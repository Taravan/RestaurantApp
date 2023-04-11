package com.isp.restaurantapp.repositories.interfaces

import com.isp.restaurantapp.BuildConfig
import com.isp.restaurantapp.models.Table
import com.isp.restaurantapp.models.dto.GoodsItemDTO
import retrofit2.http.GET
import retrofit2.http.Headers

interface GoodsGetterService {
    // Const defined in build.gradle as a config variable
    // Interface which uses Retrofit to make an API Call
    // Response is mapped to List<GoodsItemDTO>
    // Table model property names MUST MATCH with Json' variables
    @Headers(BuildConfig.API_KEY_HEADER)
    @GET(BuildConfig.SELECT_GOODS)
    suspend fun getGoods(): List<GoodsItemDTO>
}