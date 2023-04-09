package com.isp.restaurantapp.models

import java.math.BigDecimal

data class OrderByTableId(
    var id: Int = 0,
    var price: BigDecimal = BigDecimal.ZERO,  // for every operation with money
    var isPaid: Boolean = false,
    var goods_id: Int = 0,
    var reciepts_id: Int? = 0,
    var user_uid: String? = "none",
    var table_id: Int = 0,
    var name: String = "",
    var description: String? = "",
    var goodsCategory_id: Int = 0
)