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
import com.isp.restaurantapp.views.customerPart.adapters.MenuHolderAdapter
import com.isp.restaurantapp.databinding.FragmentMenuHolderBinding
import com.isp.restaurantapp.viewModels.MenuHolderVM

class MenuHolderFragment : Fragment() {

    private lateinit var viewModel: MenuHolderVM
    private lateinit var binding: FragmentMenuHolderBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_menu_holder, container, false)
        viewModel = ViewModelProvider(this)[MenuHolderVM::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        // PREPARE DATA, this should not be in init() method of VM
        viewModel.getCategories()

        val adapter = MenuHolderAdapter(viewModel)
        binding.viewPagerMenu.adapter = adapter

        var tabLayoutMediator: TabLayoutMediator? = null

//        viewModel.isDatasetInitiated.observe(viewLifecycleOwner){
//            TabLayoutMediator(binding.tabLayoutMenu, binding.viewPagerMenu) { tab, position ->
//                tab.text = viewModel.menuCategories.value?.get(position)?.categoryName
//            }.attach()
//        }

//        TabLayoutMediator(binding.tabLayoutMenu, binding.viewPagerMenu) { tab, position ->
//            tab.text = viewModel.menuCategories.value?.get(position)?.categoryName
//        }.attach()

        viewModel.menuCategories.observe(viewLifecycleOwner) {listOfCategories ->
            adapter.updateData(listOfCategories)

            tabLayoutMediator?.detach()
            tabLayoutMediator = TabLayoutMediator(binding.tabLayoutMenu, binding.viewPagerMenu) { tab, position ->
                tab.text = listOfCategories[position].categoryName
            }

            tabLayoutMediator?.attach()

        }

        return binding.root
    }
}