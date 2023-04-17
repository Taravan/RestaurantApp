package com.isp.restaurantapp.repositories

import com.isp.restaurantapp.repositories.interfaces.*

abstract class RepositoryAbstract:
    OrdersByTableIdGetterService,
    UnpaidOrdersByTableIdGetterService,
    TableGetterService,
    GoodsGetterService,
    OrderInserterService
{
}