package com.isp.restaurantapp.repositories.interfaces

import com.isp.restaurantapp.models.OrderByTableId
import retrofit2.http.GET
import retrofit2.http.Query

interface UnpaidOrdersByTableIdGetterService {

    @GET("select_unpaid_orders_by_table_id.php")
    suspend fun getUnpaidOrdersByTableId(@Query("table_id") tableId: Int): List<OrderByTableId>
}