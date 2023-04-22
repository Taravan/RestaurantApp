package com.isp.restaurantapp.repositories

import com.isp.restaurantapp.repositories.interfaces.*

abstract class RepositoryAbstract:
    OrdersByTableIdGetterService,
    UnpaidOrdersByTableIdGetterService,
    TableGetterService,
    TableUpdaterService,
    TableInserterService,
    GoodsGetterService,
    OrderInserterService,
    ReceiptInsertedService,
    OrdersUpdateReceiptIdService,
    StaffAccountGetterService,
    CategoryGetterService,
    CategoryInserterService,
    CategoryUpdaterService
{
}