package com.isp.restaurantapp.repositories

import com.isp.restaurantapp.models.InsertedId
import com.isp.restaurantapp.models.dto.CategoryDTO
import com.isp.restaurantapp.models.dto.OrderByTableIdDTO
import com.isp.restaurantapp.models.dto.TableDTO
import com.isp.restaurantapp.models.dto.GoodsItemDTO
import okhttp3.ResponseBody
import retrofit2.Response
import java.math.BigDecimal

class RepositoryDataMock: RepositoryAbstract() {

    private val tables = listOf(
        TableDTO(0, 5, "kodStoluCisloPet"),
        TableDTO(1, 10, "kodStoluCisloDeset"),
        TableDTO(2, 15, "kodStoluCisloPatnact")
    )

    private val goodsCategories = listOf(
        CategoryDTO(0, "Pečivo"),
        CategoryDTO(1, "Uzeniny")
    )
/*
    private val goods = listOf(
        GoodsItem(0, "Rohlik", "Toto je rohlik.", 0, "Pečivo", BigDecimal("5.50")),
        GoodsItem(1, "Klobása", "Toto je klobasa.", 1, "Uzeniny",BigDecimal("35.90")),
        GoodsItem(2, "Párek", "Toto je párek.", 1, "Uzeniny", BigDecimal("20.00")),
        GoodsItem(3, "Houska", "Toto je houska.", 0, "Pečivo", BigDecimal("6.50")),
        GoodsItem(4, "Kobliha", "Toto je kobliha.", 0, "Pečivo", BigDecimal("10.90"))
    )
 */
    private val goods2 = listOf(
        GoodsItemDTO(0, "Rohlik", "Toto je rohlik.",  "Pečivo", 0, 7, "mléko",  BigDecimal("5.50")),
        GoodsItemDTO(1, "Klobása", "Toto je klobasa.", "Uzeniny", 1, 7, "mléko",BigDecimal("35.90")),
        GoodsItemDTO(2, "Párek", "Toto je párek.", "Uzeniny",1, 7, "mléko", BigDecimal("20.00")),
        GoodsItemDTO(3, "Houska", "Toto je houska.",  "Pečivo", 0, 7, "mléko", BigDecimal("6.50")),
        GoodsItemDTO(4, "Kobliha", "Toto je kobliha.", "Pečivo",0, 7, "mléko", BigDecimal("10.90"))
    )

    private val unpaidGoods = listOf(
        GoodsItemDTO(0, "Rohlik", "Toto je rohlik.",  "Pečivo", 0, 7, "mléko",  BigDecimal("5.50")),
        GoodsItemDTO(4, "Kobliha", "Toto je kobliha.", "Pečivo",0, 7, "mléko", BigDecimal("10.90"))
    )

    private val unpaidOrders = listOf(
        OrderByTableIdDTO(1, BigDecimal.TEN, 1, null, null, 10, "Pivo", null, 1),
        OrderByTableIdDTO(1, BigDecimal(38), 2, null, null, 10, "Pivo2", null, 1)
    )
/*
    getTables(): List<Table> {
        return tables
    }
*/
    fun getTableById(id: Int): TableDTO {
        return tables.find { it.id == id }!!
    }

    fun getCategories(): List<CategoryDTO>{
        return goodsCategories
    }

    fun getItems(): List<GoodsItemDTO>{
        return goods2
    }

    fun getItemsById(id: Int): List<GoodsItemDTO> {
        return goods2.filter { it.categoryId == id }
    }

    fun getUnpaidOrdersByTableId(): List<GoodsItemDTO> {
        return  unpaidGoods
    }

    override suspend fun getTables(): List<TableDTO> {
        return tables
    }

    override suspend fun getGoods(): List<GoodsItemDTO> {
        return goods2
    }

    override suspend fun insertOrder(
        price: Double,
        userId: String,
        goodsId: Int,
        tableId: Int
    ): Response<InsertedId> {
        TODO("Not yet implemented")
    }


    override suspend fun getOrdersByTableId(tableId: Int): List<OrderByTableIdDTO> {
        TODO("Not yet implemented")
    }

    override suspend fun getUnpaidOrdersByTableId(tableId: Int): List<OrderByTableIdDTO> {
        return unpaidOrders
    }

}