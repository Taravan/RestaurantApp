package com.isp.restaurantapp.views.staffPart.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.isp.restaurantapp.R
import com.isp.restaurantapp.databinding.StaffRvProcessedOrderBinding
import com.isp.restaurantapp.models.dto.FrbOrderDTO
import com.isp.restaurantapp.viewModels.StaffTerminalHolderVM

class ProcessedOrdersAdapter(private val viewModel: StaffTerminalHolderVM, private var processedOrders: List<FrbOrderDTO> = emptyList()):
    RecyclerView.Adapter<ProcessedOrdersAdapter.ProcessedOrderViewHolder>(){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProcessedOrdersAdapter.ProcessedOrderViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<StaffRvProcessedOrderBinding>(
            inflater,
            R.layout.staff_rv_processed_order,
            parent,
            false
        )
        return ProcessedOrderViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return processedOrders.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newProcessedOrders: List<FrbOrderDTO>) {
        this.processedOrders = newProcessedOrders
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(
        holder: ProcessedOrdersAdapter.ProcessedOrderViewHolder,
        position: Int
    ) {
        holder.bind(processedOrders[position], viewModel)
    }

    inner class ProcessedOrderViewHolder(private val binding: StaffRvProcessedOrderBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(order: FrbOrderDTO, viewModel: StaffTerminalHolderVM){

            binding.order = order

            binding.cardProcessedHolder.setOnClickListener {
                viewModel.processProcessedOrder(order.orderId)
            }

            binding.executePendingBindings()

        }

    }

}