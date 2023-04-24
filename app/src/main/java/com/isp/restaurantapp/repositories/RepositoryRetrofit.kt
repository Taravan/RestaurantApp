package com.isp.restaurantapp.repositories

import android.util.Log
import com.isp.restaurantapp.BuildConfig
import com.isp.restaurantapp.models.InsertedId
import com.isp.restaurantapp.models.StaffAccount
import com.isp.restaurantapp.models.dto.*
import com.isp.restaurantapp.repositories.interfaces.*
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RepositoryRetrofit(
    private val url: String = BuildConfig.API_URL
): RepositoryAbstract() {
    companion object{
        private const val TAG = "RepositoryRetrofit"
    }

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

    private val receiptInserter: ReceiptInsertedService by lazy {
        _apiService.create(ReceiptInsertedService::class.java)
    }
    private val _ordersReceiptIdUpdater: OrdersUpdateReceiptIdService by lazy {
        _apiService.create(OrdersUpdateReceiptIdService::class.java)
    }

    private val _staffAccountGetter: StaffAccountGetterService by lazy{
        _apiService.create(StaffAccountGetterService::class.java)
    }

    private val _tableUpdater: TableUpdaterService by lazy{
        _apiService.create(TableUpdaterService::class.java)
    }

    private val _tableInserter: TableInserterService by lazy{
        _apiService.create(TableInserterService::class.java)
    }

    private val _tableDeleter: TableDeleterService by lazy{
        _apiService.create(TableDeleterService::class.java)
    }

    private val _categoryGetter: CategoryGetterService by lazy{
        _apiService.create(CategoryGetterService::class.java)
    }

    private val _categoryInserter: CategoryInserterService by lazy {
        _apiService.create(CategoryInserterService::class.java)
    }

    private val _categoryUpdater: CategoryUpdaterService by lazy{
        _apiService.create(CategoryUpdaterService::class.java)
    }

    private val _categoryDeleter: CategoryDeleterService by lazy{
        _apiService.create(CategoryDeleterService::class.java)
    }

    private val _allergensGetter: AllergenGetterService by lazy{
        _apiService.create(AllergenGetterService::class.java)
    }

    private val _goodsInserter: GoodsItemInserterService by lazy{
        _apiService.create(GoodsItemInserterService::class.java)
    }

    private val _goodsUpdater: GoodsItemUpdaterService by lazy {
        _apiService.create(GoodsItemUpdaterService::class.java)
    }

    private val _goodsDeleter: GoodsItemDeleterService by lazy{
        _apiService.create(GoodsItemDeleterService::class.java)
    }

    private val _allergensForItem: AllergensForGoodsItemGetter by lazy{
        _apiService.create(AllergensForGoodsItemGetter::class.java)
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

    override suspend fun updateTable(
        id: Int,
        tableNumber: Int,
        qrCode: String
    ): Response<InsertedId> {
        return _tableUpdater.updateTable(id, tableNumber, qrCode)
    }

    override suspend fun insertTable(tableNumber: Int, qrCode: String): Response<InsertedId> {
        return _tableInserter.insertTable(tableNumber, qrCode)
    }

    override suspend fun deleteTable(id: Int): Response<InsertedId> {
        return _tableDeleter.deleteTable(id)
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

    override suspend fun insertReceipt(
        personalId: Int,
        tableId: Int,
        date: String
    ): Response<InsertedId> {
        return receiptInserter.insertReceipt(personalId, tableId, date)
    }

    override suspend fun getStaffAccount(account: String, password: String): Response<List<StaffAccount>> {
        return _staffAccountGetter.getStaffAccount(account, password)
    }

    override suspend fun getCategories(): Response<List<CategoryDTO>> {
        return _categoryGetter.getCategories()
    }

    override suspend fun insertCategory(name: String, desc: String?): Response<InsertedId> {
        return _categoryInserter.insertCategory(name, desc)
    }

    override suspend fun updateCategory(id: Int, name: String, desc: String?): Response<InsertedId> {
        return _categoryUpdater.updateCategory(id, name, desc)
    }

    override suspend fun deleteCategory(id: Int): Response<InsertedId> {
        return _categoryDeleter.deleteCategory(id)
    }

    override suspend fun getAllergens(): Response<List<AllergenDTO>> {
        return _allergensGetter.getAllergens()
    }

    override suspend fun getAllergensForGoodsItem(goodsId: Int): Response<List<AllergenDTO>> {
        return _allergensForItem.getAllergensForGoodsItem(goodsId)
    }

    override suspend fun updateOrdersReceiptId(orderIdsWithReceipt: OrderIdsWithReceiptIdDTO): Response<InsertedId> {
        return _ordersReceiptIdUpdater.updateOrdersReceiptId(orderIdsWithReceipt)
    }

    override suspend fun insertGoodsItemWithAllergens(goodsWithAllergens: InsertGoodsItemDTO): Response<InsertedId> {
        return _goodsInserter.insertGoodsItemWithAllergens(goodsWithAllergens)
    }

    override suspend fun updateGoodsItemWithAllergens(goodsWithAllergens: UpdateGoodsItemDTO): Response<InsertedId> {
        return _goodsUpdater.updateGoodsItemWithAllergens(goodsWithAllergens)
    }

    override suspend fun deleteGoodsItem(id: Int): Response<InsertedId> {
        return _goodsDeleter.deleteGoodsItem(id)
    }

}