package com.isp.restaurantapp.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.isp.restaurantapp.R
import com.isp.restaurantapp.databinding.FragmentMenuBinding
import com.isp.restaurantapp.models.MenuCategory
import com.isp.restaurantapp.viewModels.MenuHolderVM

class MenuHolderAdapter(private val viewModel: MenuHolderVM, private var menuCategoriesList: List<MenuCategory> = emptyList()):
        RecyclerView.Adapter<MenuHolderAdapter.MenuCategoriesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuCategoriesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<FragmentMenuBinding>(
            inflater,
            R.layout.fragment_menu,
            parent,
            false
        )
        return MenuCategoriesViewHolder(binding)
    }

    override fun getItemCount(): Int {
        Log.d("debug", menuCategoriesList.size.toString())
        return menuCategoriesList.size
    }

    override fun onBindViewHolder(holder: MenuCategoriesViewHolder, position: Int) {
        holder.bind(menuCategoriesList[position], viewModel)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newCategoriesList: List<MenuCategory>) {
        this.menuCategoriesList = newCategoriesList
        notifyDataSetChanged()
    }

    inner class MenuCategoriesViewHolder(private val binding: FragmentMenuBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun bind(category: MenuCategory, viewModel: MenuHolderVM) {
                val adapter = MenuAdapter(category.itemsOfCategory, viewModel)
                val items = category.itemsOfCategory

                val recyclerView = binding.itemsRecyclerView
                recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
                recyclerView.adapter = MenuAdapter(category.itemsOfCategory, viewModel)

                binding.category = category
                binding.viewModel = viewModel

                //binding.itemsRecyclerView.adapter = adapter
                binding.category = category
                binding.executePendingBindings()

            }

    }
}
