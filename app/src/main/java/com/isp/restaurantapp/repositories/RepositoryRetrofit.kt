package com.isp.restaurantapp.repositories

import com.isp.restaurantapp.BuildConfig
import com.isp.restaurantapp.models.OrderByTableId
import com.isp.restaurantapp.models.Table
import com.isp.restaurantapp.repositories.interfaces.OrdersByTableIdGetterService
import com.isp.restaurantapp.repositories.interfaces.TableGetterService
import com.isp.restaurantapp.repositories.interfaces.UnpaidOrdersByTableIdGetterService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RepositoryRetrofit(
    private val url: String = BuildConfig.API_URL
): RepositoryAbstract() {

    private val _apiService: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val tableGetterService: TableGetterService by lazy {
        _apiService.create(TableGetterService::class.java)  // instantiate api interface
    }

    private val unpaidOrdersByTableIdGetterService: UnpaidOrdersByTableIdGetterService by lazy {
        _apiService.create(UnpaidOrdersByTableIdGetterService::class.java)
    }

    private val ordersByTableIdGetterService: OrdersByTableIdGetterService by lazy{
        _apiService.create(OrdersByTableIdGetterService::class.java)
    }


    override suspend fun getOrdersByTableId(tableId: Int): List<OrderByTableId> {
        return ordersByTableIdGetterService.getOrdersByTableId(tableId)
    }

    override suspend fun getUnpaidOrdersByTableId(tableId: Int): List<OrderByTableId> {
        return unpaidOrdersByTableIdGetterService.getUnpaidOrdersByTableId(tableId)
    }

    override suspend fun getTables(): List<Table> {
        return tableGetterService.getTables()
    }
}