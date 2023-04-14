package com.isp.restaurantapp.views.customerPart.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.isp.restaurantapp.R
import com.isp.restaurantapp.databinding.ItemPayBinding
import com.isp.restaurantapp.models.dto.OrderByTableIdDTO
import com.isp.restaurantapp.viewModels.PayVM

class PayAdapter(private val viewModel: PayVM, private var unpaidList: List<OrderByTableIdDTO> = emptyList()):
    RecyclerView.Adapter<PayAdapter.UnpaiedItemsViewHolder>(){

    private var selectedItems = mutableSetOf<OrderByTableIdDTO>()
    fun getSelectedItems(): Set<OrderByTableIdDTO> {
        return selectedItems
    }

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
        return unpaidList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newUnpaidItemsList: List<OrderByTableIdDTO>) {
        this.unpaidList = newUnpaidItemsList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: UnpaiedItemsViewHolder, position: Int) {
        holder.bind(unpaidList[position], viewModel)
    }

    inner class UnpaiedItemsViewHolder(private val binding: ItemPayBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: OrderByTableIdDTO, viewModel: PayVM){

            binding.item = item

            binding.checkItemToPay.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    viewModel.selectedItemsToPay.add(item)
                } else {
                    viewModel.selectedItemsToPay.remove(item)
                }
            }

            binding.executePendingBindings()

        }

        fun selectAll(isChecked: Boolean){
            for (i in unpaidList.indices) {
                binding.checkItemToPay.isChecked = isChecked
            }
        }

    }

}