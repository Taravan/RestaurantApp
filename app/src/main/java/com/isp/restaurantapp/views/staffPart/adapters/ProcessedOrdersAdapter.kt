package com.isp.restaurantapp.views.staffPart.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
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
                viewOrder(binding.root.context, order)
            }

            binding.executePendingBindings()

        }

        private fun viewOrder(context: Context, order: FrbOrderDTO){
            val builder = AlertDialog.Builder(context)
            builder.setTitle(R.string.string_buy_confirmation_title)

            val msg: String = context.getString(
                R.string.string_order_info_text,
                order.itemName, "${order.tableNumber}"
            )
            builder.setMessage(msg)

            builder.setPositiveButton("Ok", null)
            val dialog = builder.create()
            dialog.show()
        }
    }

}