package com.isp.restaurantapp.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.isp.restaurantapp.R
import com.isp.restaurantapp.databinding.TableRvRowBinding
import com.isp.restaurantapp.models.Table
import com.isp.restaurantapp.viewModels.MainActivityVM
import com.isp.restaurantapp.viewModels.MainActivityVMOLD

class TablesBindableAdapter (
    private val viewModel: MainActivityVMOLD,
    private var dataset: List<Table> = emptyList()
) : RecyclerView.Adapter<TablesBindableAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<TableRvRowBinding>(
            layoutInflater,
            R.layout.table_rv_row,
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val myData = dataset[position]
        holder.bind(myData)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    fun updateData(newDataset: List<Table>){
        this.dataset = newDataset
        notifyDataSetChanged()
    }

    inner class MyViewHolder(private val binding: TableRvRowBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(table: Table) {
            binding.table = table
            // binding.viewModel = viewModel

            binding.button.setOnClickListener {
                viewModel.onTableButtonClick(table)
            }

            binding.executePendingBindings()
        }
    }
}

/**
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.isp.restaurantapp.R
import com.isp.restaurantapp.databinding.TableRvRowBinding
import com.isp.restaurantapp.models.Table
import com.isp.restaurantapp.viewModels.MainActivityVM

class TablesBindableAdapter (
    private val viewModel: MainActivityVM,
    private var dataset: List<Table> = emptyList()
) : RecyclerView.Adapter<TablesBindableAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<TableRvRowBinding>(
            layoutInflater,
            R.layout.table_rv_row,
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val myData = dataset[position]
        holder.bind(myData)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    fun updateData(newDataset: List<Table>){
        this.dataset = newDataset
        notifyDataSetChanged()
    }

    inner class MyViewHolder(private val binding: TableRvRowBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(table: Table, viewModel: MainActivityVM) {
            binding.table = table
            // binding.viewModel = viewModel

            binding.table.set
        }
    }
}*/