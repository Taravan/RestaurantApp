package com.isp.restaurantapp.models

/**
 * Names of properties must corespond with names of columns in DB
 */
data class Table(
    var id: Int = 0,
    var tableNumber: Int = 0,
    val qrCode: String
): BaseModel (id)

