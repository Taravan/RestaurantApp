package com.isp.restaurantapp.repositories.interfaces

import com.isp.restaurantapp.BuildConfig
import com.isp.restaurantapp.models.InsertedId
import retrofit2.Response
import retrofit2.http.*

interface GoodsItemDeleterService {
    @Headers(BuildConfig.API_KEY_HEADER)
    @FormUrlEncoded
    @POST(BuildConfig.DELETE_GOODS_ITEM)
    suspend fun deleteGoodsItem(
        @Field("id") id: Int,
    ): Response<InsertedId>
}