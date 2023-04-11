package com.isp.restaurantapp.repositories

import com.isp.restaurantapp.models.Category
import com.isp.restaurantapp.models.GoodsItem
import com.isp.restaurantapp.models.OrderByTableId
import com.isp.restaurantapp.models.Table
import com.isp.restaurantapp.models.dto.GoodsItemDTO
import java.math.BigDecimal

class RepositoryDataMock: RepositoryAbstract() {

    private val tables = listOf(
        Table(0, 5, "kodStoluCisloPet"),
        Table(1, 10, "kodStoluCisloDeset"),
        Table(2, 15, "kodStoluCisloPatnact")
    )

    private val goodsCategories = listOf(
        Category(0, "Pečivo"),
        Category(1, "Uzeniny")
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
        OrderByTableId(1, BigDecimal.TEN, false, 1, null, null, 10, "Pivo", null, 1),
        OrderByTableId(1, BigDecimal(38), false, 2, null, null, 10, "Pivo2", null, 1)
    )
/*
    getTables(): List<Table> {
        return tables
    }
*/
    fun getTableById(id: Int): Table{
        return tables.find { it.id == id }!!
    }

    fun getCategories(): List<Category>{
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

    override suspend fun getTables(): List<Table> {
        return tables
    }

    override suspend fun getGoods(): List<GoodsItemDTO> {
        return goods2
    }


    override suspend fun getOrdersByTableId(tableId: Int): List<OrderByTableId> {
        TODO("Not yet implemented")
    }

    override suspend fun getUnpaidOrdersByTableId(tableId: Int): List<OrderByTableId> {
        return unpaidOrders
    }

}