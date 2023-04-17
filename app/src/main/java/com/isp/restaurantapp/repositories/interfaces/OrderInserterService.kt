package com.isp.restaurantapp.repositories.interfaces

import com.isp.restaurantapp.BuildConfig
import com.isp.restaurantapp.models.InsertedId
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface OrderInserterService {
    @FormUrlEncoded
    @POST(BuildConfig.INSERT_ORDER)
    suspend fun insertOrder(
        @Field("price") price: Double,
        @Field("user_uid") userId: String,
        @Field("Goods_id") goodsId: Int,
        @Field("table_id") tableId: Int
    ): Response<InsertedId>
}