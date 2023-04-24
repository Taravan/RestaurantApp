package com.isp.restaurantapp.repositories.interfaces

import com.isp.restaurantapp.BuildConfig
import com.isp.restaurantapp.models.InsertedId
import com.isp.restaurantapp.models.dto.UpdateGoodsItemDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface GoodsItemUpdaterService {
    @Headers(BuildConfig.API_KEY_HEADER)
    @POST(BuildConfig.UPDATE_GOODS_WITH_ALLERGENS)
    suspend fun updateGoodsItemWithAllergens(
        @Body goodsWithAllergens: UpdateGoodsItemDTO
    ): Response<InsertedId>
}