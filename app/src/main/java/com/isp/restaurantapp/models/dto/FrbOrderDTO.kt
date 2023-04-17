package com.isp.restaurantapp.models.dto

import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import kotlinx.parcelize.Parcelize

@Parcelize
data class FrbOrderDTO(
    @DocumentId val id: String?,
    val orderId: Int = -1,
    val itemId: Int = -1,
    val itemName: String = "",
    var lastUpdate: Timestamp = Timestamp.now(),
    val price: Double = 0.0,
    val receiptId: Int = -1,
    var state: String = "",
    val tableId: Int = -1,
    var uid: String = ""
) : Parcelable{
    constructor(): this(
        null, -1, -1, "", Timestamp.now(), 0.0, -1,"",-1,""
    )
}