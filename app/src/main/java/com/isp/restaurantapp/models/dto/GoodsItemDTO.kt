package com.isp.restaurantapp.models.dto

import java.math.BigDecimal

data class GoodsItemDTO(
    var goodsId: Int = 0,
    var goodsName: String = "",
    var goodsDesc: String? = "",

    var categoryName: String= "",
    var categoryId: Int = 0,

    var allergenId: Int? = 0,
    var allergenName: String? = "",

    var price: BigDecimal = BigDecimal.ZERO
)
