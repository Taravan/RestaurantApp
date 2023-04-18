package com.isp.restaurantapp.models

import com.google.firebase.Timestamp
import com.isp.restaurantapp.models.dto.FrbOrderDTO
import com.isp.restaurantapp.models.dto.GoodsItemDTO
import com.isp.restaurantapp.models.firebase.FrbFieldsOrders

object FrbOrderMapper {
    fun toFrbOrderMap(order: FrbOrderDTO): HashMap<String, Comparable<*>> {
        return hashMapOf(
            FrbFieldsOrders.FIELD_ORDER_ID to order.orderId,
            FrbFieldsOrders.FIELD_ITEM_ID to order.itemId,
            FrbFieldsOrders.FIELD_ITEM_NAME to order.itemName,
            FrbFieldsOrders.FIELD_LAST_UPDATE to order.lastUpdate,
            FrbFieldsOrders.FIELD_PRICE to order.price,
            FrbFieldsOrders.FIELD_RECEIPT_ID to order.receiptId,
            FrbFieldsOrders.FIELD_PAYMENT_STATE to order.state,
            FrbFieldsOrders.FIELD_TABLE_ID to order.tableId,
            FrbFieldsOrders.FIELD_TABLE_NUMBER to order.tableNumber,
            FrbFieldsOrders.FIELD_UID to order.uid
        )
    }

//    fun toFrbOrderDTO(
//        orderByTable: OrderByTableIdDTO,
//        paymentState: FrbFieldsOrders.States,
//        uid: String = ""): FrbOrderDTO {
//
//        val o = orderByTable
//        val timestamp = Timestamp.now()
//        return FrbOrderDTO(
//            o.id,
//            o.goodsId,
//            o.goodsName,
//            timestamp,
//            o.price.toDouble(),
//            o.receiptsId ?: -1,
//            paymentState.value,
//            o.tableId,
//            uid
//        )
//    }

    fun toFrbOrderDTO(
        goodsItemDTO: GoodsItemDTO, tableId: Int, tableNumber: Int,
        paymentState: FrbFieldsOrders.States = FrbFieldsOrders.States.PENDING,
        uid: String = ""): FrbOrderDTO {

        val g = goodsItemDTO
        val timestamp = Timestamp.now()
        return FrbOrderDTO(
            id = null,
            itemId = g.goodsId, itemName = g.goodsName,
            price = g.price.toDouble(),
            state = paymentState.value,
            lastUpdate = timestamp,
            tableId = tableId, tableNumber = tableNumber,
            uid = uid
        )
    }

}