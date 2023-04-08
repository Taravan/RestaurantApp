package com.isp.restaurantapp.views.customerPart.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.isp.restaurantapp.R
import com.isp.restaurantapp.databinding.ItemMenuBinding
import com.isp.restaurantapp.models.MenuItem
import com.isp.restaurantapp.viewModels.MenuHolderVM

class MenuAdapter(private var itemsList: List<MenuItem> = emptyList(), private val viewModel: MenuHolderVM):
    RecyclerView.Adapter<MenuAdapter.ItemsViewHolder>() {

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
    fun updateData(newMenuItemList: List<MenuItem>) {
        this.itemsList = newMenuItemList
        notifyDataSetChanged()
    }

    inner class ItemsViewHolder(private val binding: ItemMenuBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(menuItem: MenuItem, viewModel: MenuHolderVM){
            binding.item = menuItem

            binding.btnOrder.setOnClickListener {
                viewModel.orderButtonClicked(menuItem)
            }

            binding.executePendingBindings()
        }
    }
}

//class MenuAdapter(private val viewModel: MenuVM, private var itemsList: List<Item> = emptyList()):
//    RecyclerView.Adapter<MenuAdapter.ItemsViewHolder>() {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsViewHolder {
//        val inflater = LayoutInflater.from(parent.context)
//        val binding = DataBindingUtil.inflate<ItemMenuBinding>(
//            inflater,
//            R.layout.item_menu,
//            parent,
//            false
//        )
//        return ItemsViewHolder(binding)
//    }
//
//    override fun getItemCount(): Int {
//        return itemsList.size
//    }
//
//    override fun onBindViewHolder(holder: ItemsViewHolder, position: Int) {
//        holder.bind(itemsList[position], viewModel)
//    }
//
//    @SuppressLint("NotifyDataSetChanged")
//    fun updateData(newItemList: List<Item>) {
//        this.itemsList = newItemList
//        notifyDataSetChanged()
//    }
//
//    inner class ItemsViewHolder(private val binding: ItemMenuBinding): RecyclerView.ViewHolder(binding.root) {
//        fun bind(item: Item, viewModel: MenuVM){
//            binding.item = item
//            binding.executePendingBindings()
//        }
//    }
//}