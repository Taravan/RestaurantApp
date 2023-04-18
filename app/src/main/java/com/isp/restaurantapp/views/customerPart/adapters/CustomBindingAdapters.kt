package com.isp.restaurantapp.views.customerPart.adapters

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.google.firebase.Timestamp
import com.isp.restaurantapp.models.firebase.FrbFieldsOrders
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

    @BindingAdapter("hideIfPending")
    @JvmStatic
    fun hideIfPending(view: View, status: String?){
        status?.let {
            if (status == FrbFieldsOrders.States.PENDING.value){
                view.visibility = View.GONE
            } else{
                view.visibility = View.VISIBLE
            }
        }
    }

}