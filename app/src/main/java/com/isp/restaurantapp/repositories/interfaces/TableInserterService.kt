package com.isp.restaurantapp.repositories.interfaces

import com.isp.restaurantapp.BuildConfig
import com.isp.restaurantapp.models.InsertedId
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface TableInserterService {
    @Headers(BuildConfig.API_KEY_HEADER)
    @FormUrlEncoded
    @POST(BuildConfig.INSERT_TABLE)
    suspend fun insertTable(
        @Field("tableNumber") tableNumber: Int,
        @Field("qrCode") qrCode: String
    ): Response<InsertedId>
}