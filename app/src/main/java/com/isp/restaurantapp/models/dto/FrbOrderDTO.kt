package com.isp.restaurantapp.models.dto

import com.google.firebase.Timestamp

data class FrbOrderDTO(
    val itemId: Int = 0,
    val itemName: String = "",
    val lastUpdate: Timestamp = Timestamp.now(),
    val price: Double = 0.0,
    val receiptId: Int = 0,
    val state: String = "",
    val tableId: Int = 0,
    val uid: String = ""
)