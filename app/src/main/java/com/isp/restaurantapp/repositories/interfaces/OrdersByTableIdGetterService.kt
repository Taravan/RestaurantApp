package com.isp.restaurantapp.repositories.interfaces

import com.isp.restaurantapp.BuildConfig
import com.isp.restaurantapp.models.dto.OrderByTableIdDTO
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface OrdersByTableIdGetterService {
    @Headers(BuildConfig.API_KEY_HEADER)
    @GET(BuildConfig.SELECT_ORDERS_BY_TABLE_ID)
    suspend fun getOrdersByTableId(@Query("table_id") tableId: Int): List<OrderByTableIdDTO>
}