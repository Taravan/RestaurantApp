package com.isp.restaurantapp.views.staffPart.adapters

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.isp.restaurantapp.R
import com.isp.restaurantapp.databinding.StaffRvOverviewOrderBinding
import com.isp.restaurantapp.models.dto.FrbOrderDTO
import com.isp.restaurantapp.viewModels.StaffOverviewVM

class OverviewOrdersAdapter(private val viewModel: StaffOverviewVM, private var items: List<FrbOrderDTO> = emptyList()):
    RecyclerView.Adapter<OverviewOrdersAdapter.OverviewOrderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OverviewOrderViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<StaffRvOverviewOrderBinding>(
            inflater,
            R.layout.staff_rv_overview_order,
            parent,
            false
        )
        return OverviewOrderViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newItems: List<FrbOrderDTO>) {
        this.items = newItems
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: OverviewOrderViewHolder, position: Int) {
        holder.bind(items[position], viewModel)
    }

    inner class OverviewOrderViewHolder(private val binding: StaffRvOverviewOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FrbOrderDTO, viewModel: StaffOverviewVM) {
            binding.item = item
            binding.executePendingBindings()

            binding.cardOrderOrverview.setOnLongClickListener {
                openDeleteOrderDialog(binding.root.context, item)
                return@setOnLongClickListener true
            }

        }
    }

    private fun openDeleteOrderDialog(context: Context, order: FrbOrderDTO) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(R.string.pay_dialog_delete_title)
        builder.setMessage(context.getString(R.string.overview_dialog_delete_message, order.itemName, order.tableNumber))
        builder.setPositiveButton(R.string.btn_yes) { _, _ ->
            viewModel.deletePendingOrder(order)
        }
        builder.setNegativeButton(R.string.btn_no, null)
        builder.create().show()
    }

}