package com.isp.restaurantapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.isp.restaurantapp.R
import com.isp.restaurantapp.databinding.ItemMenuBinding
import com.isp.restaurantapp.models.Item
import com.isp.restaurantapp.viewModels.MenuVM
import com.isp.restaurantapp.views.customerPart.MenuFragment

class MenuHolderAdapter(private val viewModel: ViewModel, private val menuCategoriesList: List<MenuFragment> = emptyList()):
        RecyclerView.Adapter<MenuHolderAdapter.MenuCategoriesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuCategoriesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemMenuBinding>(
            inflater,
            R.layout.fragment_menu,
            parent,
            false
        )
        return MenuCategoriesViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return menuCategoriesList.size
    }

    override fun onBindViewHolder(holder: MenuCategoriesViewHolder, position: Int) {
        TODO()
    }

    inner class MenuCategoriesViewHolder(private val binding: ItemMenuBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }
}
