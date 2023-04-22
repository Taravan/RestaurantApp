package com.isp.restaurantapp.views.staffPart.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.isp.restaurantapp.R
import com.isp.restaurantapp.databinding.StaffRvTableOverviewBinding
import com.isp.restaurantapp.models.dto.TableDTO
import com.isp.restaurantapp.viewModels.StaffGoodsVM

class TablesOverviewAdapter(private val viewModel: StaffGoodsVM, private var tables: List<TableDTO> = emptyList()):
    RecyclerView.Adapter<TablesOverviewAdapter.TableOverviewViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TableOverviewViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<StaffRvTableOverviewBinding>(
            inflater,
            R.layout.staff_rv_table_overview,
            parent,
            false
        )
        return TableOverviewViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return tables.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newTables: List<TableDTO>) {
        this.tables = newTables
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: TableOverviewViewHolder, position: Int) {
        holder.bind(tables[position], viewModel)
    }

    inner class TableOverviewViewHolder(private val binding: StaffRvTableOverviewBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(table: TableDTO, viewModel: StaffGoodsVM){
            binding.table = table

            binding.cardTableOverview.setOnClickListener {

            }

            binding.executePendingBindings()

        }

    }

}