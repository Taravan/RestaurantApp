package com.isp.restaurantapp.models.dto

import android.os.Parcelable
import com.isp.restaurantapp.models.BaseModel
import kotlinx.parcelize.Parcelize

/**
 * Names of properties must corespond with names of columns in DB
 */

/**
 * Object passed between activities must implement Parcelable interface
 *  and have Parcelize annotation so it can implement itself and we don't need
 *  to update the code after every update of the data class
 */

@Parcelize
data class TableDTO(
    var id: Int = 0,
    var tableNumber: Int = 0,
    var qrCode: String
): BaseModel(id), Parcelable

