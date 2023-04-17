package com.isp.restaurantapp.repositories

import com.isp.restaurantapp.BuildConfig
import com.isp.restaurantapp.models.InsertedId
import com.isp.restaurantapp.models.dto.OrderByTableIdDTO
import com.isp.restaurantapp.models.dto.TableDTO
import com.isp.restaurantapp.models.dto.GoodsItemDTO
import com.isp.restaurantapp.repositories.interfaces.*
import okhttp3.ResponseBody
import retrofit2.Response
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
    private val goodsGetterService: GoodsGetterService by lazy {
        _apiService.create(GoodsGetterService::class.java)  // instantiate api interface
    }

    private val unpaidOrdersByTableIdGetterService: UnpaidOrdersByTableIdGetterService by lazy {
        _apiService.create(UnpaidOrdersByTableIdGetterService::class.java)
    }

    private val ordersByTableIdGetterService: OrdersByTableIdGetterService by lazy{
        _apiService.create(OrdersByTableIdGetterService::class.java)
    }

    private val orderInserter: OrderInserterService by lazy {
        _apiService.create(OrderInserterService::class.java)
    }


    override suspend fun getOrdersByTableId(tableId: Int): List<OrderByTableIdDTO> {
        return ordersByTableIdGetterService.getOrdersByTableId(tableId)
    }

    override suspend fun getUnpaidOrdersByTableId(tableId: Int): List<OrderByTableIdDTO> {
        return unpaidOrdersByTableIdGetterService.getUnpaidOrdersByTableId(tableId)
    }

    override suspend fun getTables(): List<TableDTO> {
        return tableGetterService.getTables()
    }

    override suspend fun getGoods(): List<GoodsItemDTO> {
        return goodsGetterService.getGoods()
    }

    override suspend fun insertOrder(
        price: Double,
        userId: String,
        goodsId: Int,
        tableId: Int
    ): Response<InsertedId> {
        return orderInserter.insertOrder(price, userId, goodsId, tableId)
    }
}