package com.isp.restaurantapp.models

import java.math.BigDecimal

data class GoodsItem(
    val id: Int = 0,
    val name: String = "",
    val description: String = "",
    val categoryId: Int = 0,
    val categoryName: String = "",
    val price: BigDecimal = BigDecimal.ZERO
    )
