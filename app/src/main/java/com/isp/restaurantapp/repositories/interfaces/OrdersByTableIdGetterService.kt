package com.isp.restaurantapp.repositories.interfaces

import com.isp.restaurantapp.models.OrderByTableId
import retrofit2.http.GET
import retrofit2.http.Query

interface OrdersByTableIdGetterService {
    @GET("select_orders_by_table_id.php")
    suspend fun getOrdersByTableId(@Query("table_id") tableId: Int): List<OrderByTableId>
}