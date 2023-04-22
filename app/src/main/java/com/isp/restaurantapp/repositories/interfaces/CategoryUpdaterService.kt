package com.isp.restaurantapp.repositories.interfaces

import com.isp.restaurantapp.BuildConfig
import com.isp.restaurantapp.models.InsertedId
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface CategoryUpdaterService{
    @Headers(BuildConfig.API_KEY_HEADER)
    @FormUrlEncoded
    @POST(BuildConfig.UPDATE_GOODS_CATEGORY)
    suspend fun updateCategory(
        @Field("id") id: Int,
        @Field("name") name: String,
        @Field("desc") desc: String?
    ): Response<InsertedId>
}