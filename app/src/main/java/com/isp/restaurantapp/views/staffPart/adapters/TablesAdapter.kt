package com.isp.restaurantapp.views.staffPart.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.isp.restaurantapp.R
import com.isp.restaurantapp.databinding.StaffRvTableBinding
import com.isp.restaurantapp.models.dto.TableDTO
import com.isp.restaurantapp.viewModels.StaffTablesVM

class TablesAdapter(private val viewModel: StaffTablesVM, private var tables: List<TableDTO> = emptyList()):
    RecyclerView.Adapter<TablesAdapter.TableViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TableViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<StaffRvTableBinding>(
            inflater,
            R.layout.staff_rv_table,
            parent,
            false
        )
        return TableViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return tables.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newTables: List<TableDTO>) {
        this.tables = newTables
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: TableViewHolder, position: Int) {
        holder.bind(tables[position], viewModel)
    }

    inner class TableViewHolder(private val binding: StaffRvTableBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(table: TableDTO, viewModel: StaffTablesVM){

            //TODO: Change color in layout condition (viewmodel)

            binding.viewModel = viewModel
            binding.table = table

            binding.cardTable.setOnClickListener {
                viewModel.onSelectTable(table.id)
            }

            binding.executePendingBindings()

        }

    }

}