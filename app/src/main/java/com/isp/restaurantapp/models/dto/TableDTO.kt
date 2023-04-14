package com.isp.restaurantapp.models.dto

import com.isp.restaurantapp.models.BaseModel

/**
 * Names of properties must corespond with names of columns in DB
 */
data class TableDTO(
    var id: Int = 0,
    var tableNumber: Int = 0,
    val qrCode: String
): BaseModel(id)

