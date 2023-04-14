package com.isp.restaurantapp.views.customerPart

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.isp.restaurantapp.R
import com.isp.restaurantapp.views.customerPart.adapters.MenuHolderAdapter
import com.isp.restaurantapp.databinding.FragmentMenuHolderBinding
import com.isp.restaurantapp.viewModels.MenuHolderVM

class MenuHolderFragment : Fragment() {
    companion object{
        const val TAG = "MenuHolderFragment"
    }

    private lateinit var viewModel: MenuHolderVM
    private lateinit var binding: FragmentMenuHolderBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_menu_holder, container, false)
        viewModel = ViewModelProvider(this)[MenuHolderVM::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        // FETCH & FETCH DATA, this should not be in init() method of VM
        // You have more control about whats happening
        viewModel.fetchData()

        val adapter = MenuHolderAdapter(viewModel)
        binding.viewPagerMenu.adapter = adapter

        var tabLayoutMediator: TabLayoutMediator? = null

        viewModel.goodsAllergens.observe(viewLifecycleOwner){
            Log.e(TAG, it.toString())
        }

        viewModel.menuCategories.observe(viewLifecycleOwner) {
            // Once the data is fetched by IO and then confirmed and exposed by Dispatchers.Main
            if (!it.isNullOrEmpty()){
                Log.e(TAG, "Dataset ready, initiating adapter & mediator update")
                adapter.updateData(it)

                tabLayoutMediator?.detach()
                tabLayoutMediator = TabLayoutMediator(binding.tabLayoutMenu, binding.viewPagerMenu) { tab, position ->
                    tab.text = it[position].categoryName
                }

                tabLayoutMediator?.attach()
            }
        }



        return binding.root
    }
}