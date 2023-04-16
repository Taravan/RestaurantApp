package com.isp.restaurantapp.views.customerPart.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.isp.restaurantapp.R
import com.isp.restaurantapp.databinding.ItemRvHistoryBinding
import com.isp.restaurantapp.models.dto.FrbOrderDTO
import com.isp.restaurantapp.viewModels.HistoryVM

class HistoryAdapter(private val viewModel: HistoryVM, private var dataset: List<FrbOrderDTO> = emptyList()):
    RecyclerView.Adapter<HistoryAdapter.HistoryItemsViewHolder>(){


    inner class HistoryItemsViewHolder(private val binding: ItemRvHistoryBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FrbOrderDTO, viewModel: HistoryVM){

            binding.item = item
            binding.vm = viewModel

            binding.executePendingBindings()

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryItemsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemRvHistoryBinding>(
            inflater,
            R.layout.item_rv_history,
            parent,
            false
        )
        return HistoryItemsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newDataset: List<FrbOrderDTO>) {
        this.dataset = newDataset
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: HistoryItemsViewHolder, position: Int) {
        holder.bind(dataset[position], viewModel)
    }


}