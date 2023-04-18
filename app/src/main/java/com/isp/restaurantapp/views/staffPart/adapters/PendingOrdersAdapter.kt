package com.isp.restaurantapp.views.staffPart.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.isp.restaurantapp.R
import com.isp.restaurantapp.databinding.ItemPayBinding
import com.isp.restaurantapp.databinding.StaffFragmentTerminalHolderBinding
import com.isp.restaurantapp.databinding.StaffRvPendingOrderBinding
import com.isp.restaurantapp.models.dto.FrbOrderDTO
import com.isp.restaurantapp.models.dto.OrderByTableIdDTO
import com.isp.restaurantapp.viewModels.PayVM
import com.isp.restaurantapp.viewModels.StaffTerminalHolderVM

class PendingOrdersAdapter(private val viewModel: StaffTerminalHolderVM, private var pendingOrders: List<FrbOrderDTO> = emptyList()):
    RecyclerView.Adapter<PendingOrdersAdapter.PendingOrderViewHolder>(){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PendingOrdersAdapter.PendingOrderViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<StaffRvPendingOrderBinding>(
            inflater,
            R.layout.staff_rv_pending_order,
            parent,
            false
        )
        return PendingOrderViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return pendingOrders.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newPendingOrders: List<FrbOrderDTO>) {
        this.pendingOrders = newPendingOrders
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(
        holder: PendingOrdersAdapter.PendingOrderViewHolder,
        position: Int
    ) {
        holder.bind(pendingOrders[position], viewModel)
    }

    inner class PendingOrderViewHolder(private val binding: StaffRvPendingOrderBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(order: FrbOrderDTO, viewModel: StaffTerminalHolderVM){

            binding.order = order

            binding.cardPendingHolder.setOnClickListener {
                viewModel.processPendingOrder(order.orderId)
            }

            binding.executePendingBindings()

        }

    }

}