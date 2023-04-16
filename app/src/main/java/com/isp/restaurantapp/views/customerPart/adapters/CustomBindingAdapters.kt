package com.isp.restaurantapp.views.customerPart.adapters

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*

object CustomBindingAdapters {

    @BindingAdapter("timestampFormatted")
    @JvmStatic
    fun TextView.setTimestampFormatted(timestamp: Timestamp?) {
        timestamp?.let {
            val format = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
            text = format.format(it.toDate())
        } ?: run {
            text = ""
        }
    }

}