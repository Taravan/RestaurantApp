package com.isp.restaurantapp.repositories.interfaces

import com.isp.restaurantapp.BuildConfig
import com.isp.restaurantapp.models.dto.AllergenDTO
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface AllergensForGoodsItemGetter {

    @Headers(BuildConfig.API_KEY_HEADER)
    @FormUrlEncoded
    @POST(BuildConfig.SELECT_ALLERGENS_FOR_ITEM)
    suspend fun getAllergensForGoodsItem(
        @Field("goodsId") goodsId: Int
    ): Response<List<AllergenDTO>>
}