package com.isp.restaurantapp.models.dto

import java.math.BigDecimal

data class OrderByTableIdDTO(
    var id: Int = 0,
    var price: BigDecimal = BigDecimal.ZERO,  // for every operation with money
    var isPaid: Boolean = false,
    var goodsId: Int = 0,
    var receiptsId: Int? = 0,
    var userId: String? = "",
    var tableId: Int = 0,
    var goodsName: String = "",
    var goodsDesc: String? = "",
    var goodsCategoryId: Int = 0
)