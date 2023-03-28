package com.isp.restaurantapp.views

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.isp.restaurantapp.R
import com.isp.restaurantapp.databinding.ActivityMainBinding
import com.isp.restaurantapp.viewModels.MainActivityVM
import com.isp.restaurantapp.views.adapters.TablesBindableAdapter

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var viewModel: MainActivityVM
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // enable data binding and provide viewmodel
        this.binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        this.viewModel = ViewModelProvider(this)[MainActivityVM::class.java]

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        // create layout manager and adapter for Recyclerview
        val layoutManager = LinearLayoutManager(this)
        val adapter = TablesBindableAdapter(viewModel)

        binding.recyclerViewTables.layoutManager = layoutManager
        binding.recyclerViewTables.adapter = adapter

        // data bind - observe data, set tables as a Recyclerviews adapter dataset
        viewModel.getTablesLiveData().observe(this) { tablesDataList ->
            adapter.updateData(tablesDataList)
        }

        // data bind onClickListener
        viewModel.selectedItem.observe(this) { selectedItem ->
            if (selectedItem != null) {
                Toast.makeText(
                    this,
                    "You clicked table number ${selectedItem.tableNumber}",
                    Toast.LENGTH_SHORT
                ).show()
                // Null the selectedItem
                viewModel.onItemClickComplete()
            }
        }

    }

}