package com.isp.restaurantapp.repositories

import com.google.firebase.Timestamp
import com.isp.restaurantapp.models.InsertedId
import com.isp.restaurantapp.models.StaffAccount
import com.isp.restaurantapp.models.dto.*
import retrofit2.Response
import java.math.BigDecimal

class RepositoryDataMock: RepositoryAbstract() {

    private val tables = listOf(
        TableDTO(1, 1, "kodStoluCisloPet"),
        TableDTO(2, 2, "kodStoluCisloDeset"),
        TableDTO(3, 3, "kodStoluCisloPatnact")
    )

    private val goodsCategories = listOf(
        CategoryDTO(0, "Pečivo", null),
        CategoryDTO(1, "Uzeniny", null)
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

    private val pendingOrders = listOf(
        FrbOrderDTO("0", 0, 0, "Pivo", Timestamp.now(), 32.0, 0, "Pending", 5, 5, "uid"),
        FrbOrderDTO("1", 1, 1, "Klobása", Timestamp.now(), 58.0, 1, "Pending", 12, 12, "uid2")
        )

    private val processedOrders = listOf(
        FrbOrderDTO("2", 5, 5, "Hranolky", Timestamp.now(), 25.0, 5, "Processed", 0, 5, "uid3"),
        FrbOrderDTO("3", 6, 6, "Pizza", Timestamp.now(), 150.0, 6, "Processed", 1, 10, "uid4"),
        FrbOrderDTO("4", 7, 7, "Párek", Timestamp.now(), 20.0, 7, "Processed", 0, 5, "uid5"),
        FrbOrderDTO("5", 8, 8, "Hamburger", Timestamp.now(), 210.0, 8, "Processed", 0, 5, "uid6"),
        FrbOrderDTO("6", 9, 9, "Kofola", Timestamp.now(), 30.0, 9, "Processed", 2, 15, "uid7"),
        FrbOrderDTO("7", 10, 10, "Rum", Timestamp.now(), 35.0, 10, "Processed", 1, 10, "uid8")
        )

/*
    getTables(): List<Table> {
        return tables
    }
*/
    fun getTableById(id: Int): TableDTO {
        return tables.find { it.id == id }!!
    }

    override suspend fun getCategories(): Response<List<CategoryDTO>> {
        TODO("Not implemented")
    }

    override suspend fun insertCategory(name: String, desc: String?): Response<InsertedId> {
        TODO("Not yet implemented")
    }

    override suspend fun updateCategory(id: Int, name: String, desc: String?): Response<InsertedId> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteCategory(id: Int): Response<InsertedId> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllergens(): Response<List<AllergenDTO>> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllergensForGoodsItem(goodsId: Int): Response<List<AllergenDTO>> {
        TODO("Not yet implemented")
    }

    override suspend fun insertGoodsItemWithAllergens(goodsWithAllergens: InsertGoodsItemDTO): Response<InsertedId> {
        TODO("Not yet implemented")
    }

    override suspend fun updateGoodsItemWithAllergens(goodsWithAllergens: UpdateGoodsItemDTO): Response<InsertedId> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteGoodsItem(id: Int): Response<InsertedId> {
        TODO("Not yet implemented")
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

    override suspend fun updateTable(
        id: Int,
        tableNumber: Int,
        qrCode: String
    ): Response<InsertedId> {
        TODO("Not yet implemented")
    }

    override suspend fun insertTable(tableNumber: Int, qrCode: String): Response<InsertedId> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTable(id: Int): Response<InsertedId> {
        TODO("Not yet implemented")
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

    override suspend fun insertReceipt(
        personalId: Int,
        tableId: Int,
        date: String
    ): Response<InsertedId> {
        TODO("Not yet implemented")
    }

    override suspend fun getStaffAccount(account: String, password: String): Response<List<StaffAccount>> {
        TODO("Not yet implemented")
    }

    override suspend fun updateOrdersReceiptId(orderIdsWithReceipt: OrderIdsWithReceiptIdDTO): Response<InsertedId> {
        TODO("Not yet implemented")
    }


    override suspend fun getOrdersByTableId(tableId: Int): List<OrderByTableIdDTO> {
        TODO("Not yet implemented")
    }

    override suspend fun getUnpaidOrdersByTableId(tableId: Int): List<OrderByTableIdDTO> {
        return unpaidOrders
    }

    // Just for testing recyclerview
    suspend fun getPendingOrders(): List<FrbOrderDTO> {
        return pendingOrders
    }

    suspend fun getProcessedOrders(): List<FrbOrderDTO> {
        return processedOrders
    }

}