package com.isp.restaurantapp.views.staffPart.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.isp.restaurantapp.R
import com.isp.restaurantapp.databinding.StaffRvLeftToPayBinding
import com.isp.restaurantapp.models.dto.FrbOrderDTO
import com.isp.restaurantapp.viewModels.StaffTablesVM


class LeftToPayAdapter(private val viewModel: StaffTablesVM, private var leftToPayList: List<FrbOrderDTO> = emptyList()):
    RecyclerView.Adapter<LeftToPayAdapter.ItemLeftToPayViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemLeftToPayViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<StaffRvLeftToPayBinding>(
            inflater,
            R.layout.staff_rv_left_to_pay,
            parent,
            false
        )
        return ItemLeftToPayViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return leftToPayList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newLeftToPayList: List<FrbOrderDTO>) {
        this.leftToPayList = newLeftToPayList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ItemLeftToPayViewHolder, position: Int) {
        holder.bind(leftToPayList[position], viewModel)
    }

    inner class ItemLeftToPayViewHolder(private val binding: StaffRvLeftToPayBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FrbOrderDTO, viewModel: StaffTablesVM){

            binding.item = item

            binding.cardItemLeftToPay.setOnClickListener {
                viewModel.onMoveItemToMarkedToPay(item.orderId)
            }

            binding.executePendingBindings()

        }

    }

}