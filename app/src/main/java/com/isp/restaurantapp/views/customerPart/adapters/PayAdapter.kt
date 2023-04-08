package com.isp.restaurantapp.views.customerPart.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.isp.restaurantapp.R
import com.isp.restaurantapp.databinding.ItemPayBinding
import com.isp.restaurantapp.models.MenuItem
import com.isp.restaurantapp.viewModels.PayVM

class PayAdapter(private val viewModel: PayVM, private var unpaiedItemsList: List<MenuItem> = emptyList()):
    RecyclerView.Adapter<PayAdapter.UnpaiedItemsViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UnpaiedItemsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemPayBinding>(
            inflater,
            R.layout.item_pay,
            parent,
            false
        )
        return UnpaiedItemsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return unpaiedItemsList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newUnpaiedItemsList: List<MenuItem>) {
        this.unpaiedItemsList = newUnpaiedItemsList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: UnpaiedItemsViewHolder, position: Int) {
        holder.bind(unpaiedItemsList[position], viewModel)
    }

    inner class UnpaiedItemsViewHolder(private val binding: ItemPayBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(menuItem: MenuItem, viewModel: PayVM){

            binding.item = menuItem

            binding.executePendingBindings()

        }
    }

}