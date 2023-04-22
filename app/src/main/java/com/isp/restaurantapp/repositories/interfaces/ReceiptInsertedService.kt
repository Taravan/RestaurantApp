package com.isp.restaurantapp.repositories.interfaces

import com.google.firebase.Timestamp
import com.isp.restaurantapp.BuildConfig
import com.isp.restaurantapp.models.InsertedId
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST
import java.text.SimpleDateFormat
import java.util.*

interface ReceiptInsertedService {
    @Headers(BuildConfig.API_KEY_HEADER)
    @FormUrlEncoded
    @POST(BuildConfig.INSERT_RECEIPT)
    suspend fun insertReceipt(
        @Field("personalId") personalId: Int,
        @Field("tableId") tableId: Int,
        @Field("date") date: String = SimpleDateFormat(
            "yyyy-MM-dd", Locale.getDefault()).format(Timestamp.now().toDate().time)
    ): Response<InsertedId>
}