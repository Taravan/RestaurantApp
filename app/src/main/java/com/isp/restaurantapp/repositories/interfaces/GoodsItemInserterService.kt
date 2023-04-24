package com.isp.restaurantapp.repositories.interfaces

import com.isp.restaurantapp.BuildConfig
import com.isp.restaurantapp.models.InsertedId
import com.isp.restaurantapp.models.dto.InsertGoodsItemDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface GoodsItemInserterService {
    @Headers(BuildConfig.API_KEY_HEADER)
    @POST(BuildConfig.INSERT_GOODS_WITH_ALLERGENS)
    suspend fun insertGoodsItemWithAllergens(
        @Body goodsWithAllergens: InsertGoodsItemDTO
    ): Response<InsertedId>
}