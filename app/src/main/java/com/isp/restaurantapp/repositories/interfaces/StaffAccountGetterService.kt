package com.isp.restaurantapp.repositories.interfaces

import com.isp.restaurantapp.BuildConfig
import com.isp.restaurantapp.models.StaffAccount
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface StaffAccountGetterService {
    @Headers(BuildConfig.API_KEY_HEADER)
    @FormUrlEncoded
    @POST(BuildConfig.SELECT_STAFF_ACCOUNT)
    suspend fun getStaffAccount(
        @Field("account") account: String,
        @Field("password") password: String
    ): Response<List<StaffAccount>>
}