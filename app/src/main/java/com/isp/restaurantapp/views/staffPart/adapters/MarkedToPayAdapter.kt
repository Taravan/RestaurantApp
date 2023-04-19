package com.isp.restaurantapp.views.staffPart.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.isp.restaurantapp.R
import com.isp.restaurantapp.databinding.StaffRvToPayBinding
import com.isp.restaurantapp.models.dto.FrbOrderDTO
import com.isp.restaurantapp.viewModels.StaffTablesVM

class MarkedToPayAdapter(private val viewModel: StaffTablesVM, private var toPayList: List<FrbOrderDTO> = emptyList()):
    RecyclerView.Adapter<MarkedToPayAdapter.ItemToPayViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemToPayViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<StaffRvToPayBinding>(
            inflater,
            R.layout.staff_rv_to_pay,
            parent,
            false
        )
        return ItemToPayViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return toPayList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newToPayList: List<FrbOrderDTO>) {
        this.toPayList = newToPayList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ItemToPayViewHolder, position: Int) {
        holder.bind(toPayList[position], viewModel)
    }

    inner class ItemToPayViewHolder(private val binding: StaffRvToPayBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FrbOrderDTO, viewModel: StaffTablesVM){

            binding.item = item

            binding.cardItemToPay.setOnClickListener {
                //viewModel.onMoveItemToLeftToPay(item.orderId)
            }

            binding.executePendingBindings()

        }

    }

}