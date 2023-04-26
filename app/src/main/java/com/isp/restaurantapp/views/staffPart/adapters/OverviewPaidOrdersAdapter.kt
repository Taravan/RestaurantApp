package com.isp.restaurantapp.views.staffPart.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.isp.restaurantapp.R
import com.isp.restaurantapp.databinding.StaffRvOverviewPaidBinding
import com.isp.restaurantapp.models.dto.FrbOrderDTO
import com.isp.restaurantapp.viewModels.StaffOverviewVM

class OverviewPaidOrdersAdapter(private val viewModel: StaffOverviewVM, private var items: List<FrbOrderDTO> = emptyList()):
    RecyclerView.Adapter<OverviewPaidOrdersAdapter.OverviewPaidOrderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OverviewPaidOrderViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<StaffRvOverviewPaidBinding>(
            inflater,
            R.layout.staff_rv_overview_paid,
            parent,
            false
        )
        return OverviewPaidOrderViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newItems: List<FrbOrderDTO>) {
        this.items = newItems
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: OverviewPaidOrderViewHolder, position: Int) {
        holder.bind(items[position], viewModel)
    }

    inner class OverviewPaidOrderViewHolder(private val binding: StaffRvOverviewPaidBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FrbOrderDTO, viewModel: StaffOverviewVM) {
            binding.item = item
            binding.executePendingBindings()
        }
    }
}