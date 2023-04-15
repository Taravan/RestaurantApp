package com.isp.restaurantapp.views.customerPart.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.isp.restaurantapp.R
import com.isp.restaurantapp.databinding.ItemMenuBinding
import com.isp.restaurantapp.models.dto.GoodsItemDTO
import com.isp.restaurantapp.viewModels.MenuHolderVM

class MenuAdapter(
    private var itemsList: List<GoodsItemDTO> = emptyList(),
    private val viewModel: MenuHolderVM):
    RecyclerView.Adapter<MenuAdapter.ItemsViewHolder>() {

    inner class ItemsViewHolder(private val binding: ItemMenuBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(goodsItem: GoodsItemDTO, viewModel: MenuHolderVM){
            binding.item = goodsItem
            binding.viewModel = viewModel


            binding.btnOrder.setOnClickListener {
                viewModel.orderButtonClicked(goodsItem)
            }

            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemMenuBinding>(
            inflater,
            R.layout.item_menu,
            parent,
            false
        )
        return ItemsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return itemsList.size
    }

    override fun onBindViewHolder(holder: ItemsViewHolder, position: Int) {
        holder.bind(itemsList[position], viewModel)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newGoodsItemList: List<GoodsItemDTO>) {
        this.itemsList = newGoodsItemList
        notifyDataSetChanged()
    }

}