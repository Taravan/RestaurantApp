package com.isp.restaurantapp.repositories

import com.isp.restaurantapp.models.OrderByTableId
import com.isp.restaurantapp.repositories.interfaces.OrdersByTableIdGetterService
import com.isp.restaurantapp.repositories.interfaces.TableGetterService
import com.isp.restaurantapp.repositories.interfaces.UnpaidOrdersByTableIdGetterService

abstract class RepositoryAbstract:
    OrdersByTableIdGetterService,
    UnpaidOrdersByTableIdGetterService,
    TableGetterService
{
}