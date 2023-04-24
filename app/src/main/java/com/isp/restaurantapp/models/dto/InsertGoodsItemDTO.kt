package com.isp.restaurantapp.models.dto

data class InsertGoodsItemDTO(
    val name: String,
    val desc: String?,
    val categoryId: Int,
    val price: Double,
    val allergensIds: List<Int>
    )
