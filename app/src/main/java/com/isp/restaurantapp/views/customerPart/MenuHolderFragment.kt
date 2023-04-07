package com.isp.restaurantapp.views.customerPart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayoutMediator
import com.isp.restaurantapp.R
import com.isp.restaurantapp.adapters.MenuHolderAdapter
import com.isp.restaurantapp.databinding.FragmentMenuHolderBinding
import com.isp.restaurantapp.viewModels.MenuHolderVM

class MenuHolderFragment : Fragment() {

    private lateinit var viewModel: MenuHolderVM
    private lateinit var binding: FragmentMenuHolderBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_menu_holder, container, false)
        viewModel = ViewModelProvider(this)[MenuHolderVM::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val adapter = MenuHolderAdapter(viewModel)
        binding.viewPagerMenu.adapter = adapter

        TabLayoutMediator(binding.tabLayoutMenu, binding.viewPagerMenu) { tab, position ->
            tab.text = viewModel.menuCategories.value?.get(position)?.nameOfcategory
        }.attach()

        viewModel.menuCategories.observe(viewLifecycleOwner) {listOfCategories ->
            adapter.updateData(listOfCategories)
        }

        return binding.root
    }
}