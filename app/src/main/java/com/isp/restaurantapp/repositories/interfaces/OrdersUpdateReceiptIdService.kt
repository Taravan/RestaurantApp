package com.isp.restaurantapp.repositories.interfaces

import com.isp.restaurantapp.BuildConfig
import com.isp.restaurantapp.models.InsertedId
import com.isp.restaurantapp.models.dto.OrderIdsWithReceiptIdDTO
import retrofit2.Response
import retrofit2.http.*
import java.util.*

interface OrdersUpdateReceiptIdService {
    @Headers(BuildConfig.API_KEY_HEADER)
    @POST(BuildConfig.UPDATE_ORDERS_RECEPIT)
    suspend fun updateOrdersReceiptId(
        @Body orderIdsWithReceipt: OrderIdsWithReceiptIdDTO
    ): Response<InsertedId>
}