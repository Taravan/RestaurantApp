package com.isp.restaurantapp.models.firebase

object FrbFieldsOrders {
    const val FIELD_ORDER_ID = "orderId"
    const val FIELD_ITEM_ID= "itemId"
    const val FIELD_ITEM_NAME = "itemName"
    const val FIELD_PRICE = "price"
    const val FIELD_TABLE_ID = "tableId"
    const val FIELD_PAYMENT_STATE = "state"
    const val FIELD_RECEIPT_ID = "receiptId"
    const val FIELD_UID = "uid"
    const val FIELD_LAST_UPDATE = "lastUpdate"

    enum class States(val value: String){
        PAID("paid"),
        PENDING("A: pending"),
        CONFIRMED("B: confirmed"),
        FOR_PAYMENT("C: for payment")
    }



}