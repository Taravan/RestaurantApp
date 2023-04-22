package com.isp.restaurantapp.views.staffPart.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.isp.restaurantapp.R
import com.isp.restaurantapp.databinding.StaffRvMenuOverviewBinding
import com.isp.restaurantapp.models.dto.GoodsItemDTO
import com.isp.restaurantapp.viewModels.StaffGoodsVM

class MenuOverviewAdapter(private val viewModel: StaffGoodsVM, private var items: List<GoodsItemDTO> = emptyList()):
    RecyclerView.Adapter<MenuOverviewAdapter.MenuOverviewViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuOverviewViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<StaffRvMenuOverviewBinding>(
            inflater,
            R.layout.staff_rv_menu_overview,
            parent,
            false
        )
        return MenuOverviewViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newItems: List<GoodsItemDTO>) {
        this.items = newItems
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MenuOverviewViewHolder, position: Int) {
        holder.bind(items[position], viewModel)
    }

    inner class MenuOverviewViewHolder(private val binding: StaffRvMenuOverviewBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: GoodsItemDTO, viewModel: StaffGoodsVM){
            binding.item = item

            binding.cardMenuOverview.setOnClickListener {

            }

            binding.executePendingBindings()

        }

    }

}