package com.isp.restaurantapp.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StaffAccount (
    val id: Int,
    val firstName: String,
    val lastName: String,
    val privileges: Int
    ): Parcelable