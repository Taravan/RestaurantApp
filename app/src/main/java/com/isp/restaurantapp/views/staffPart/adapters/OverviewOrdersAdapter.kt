package com.isp.restaurantapp.views.staffPart.adapters

import android.annotation.SuppressLint
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

        }

    }
}