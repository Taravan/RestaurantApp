package com.isp.restaurantapp.views.customerPart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.isp.restaurantapp.R
import com.isp.restaurantapp.adapters.MenuAdapter
import com.isp.restaurantapp.databinding.FragmentMenuBinding
import com.isp.restaurantapp.repositories.dataMock
import com.isp.restaurantapp.viewModels.MenuVM
import com.isp.restaurantapp.viewModels.MenuVMFactory


class MenuFragment : Fragment() {

    private lateinit var factory: MenuVMFactory
    private lateinit var viewModel: MenuVM
    //private lateinit var binding: ActivityMainBinding
    private lateinit var binding: FragmentMenuBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_menu, container, false)
        val rep = dataMock()
        factory = MenuVMFactory(rep)
        viewModel = ViewModelProvider(this, factory)[MenuVM::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = this


        // adapter and layout
        val layoutManager = LinearLayoutManager(requireContext())
        val adapter = MenuAdapter(viewModel)
        binding.itemsRecyclerView.layoutManager = layoutManager
        binding.itemsRecyclerView.adapter = adapter

        //databind
        viewModel.items.observe(viewLifecycleOwner) {listOfItems ->
            adapter.updateData(listOfItems)
        }

        return binding.root
    }
}