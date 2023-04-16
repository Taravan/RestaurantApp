package com.isp.restaurantapp.models.dto

import com.google.firebase.Timestamp

data class FrbOrderDTO(
    val orderId: Int = -1,
    val itemId: Int = -1,
    val itemName: String = "",
    val lastUpdate: Timestamp = Timestamp.now(),
    val price: Double = 0.0,
    val receiptId: Int = -1,
    val state: String = "",
    val tableId: Int = -1,
    var uid: String = ""
)