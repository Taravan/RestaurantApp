package com.isp.restaurantapp.views.staffPart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.isp.restaurantapp.databinding.StaffFragmentOverviewBinding
import com.isp.restaurantapp.viewModels.StaffOverviewVM
import com.isp.restaurantapp.views.staffPart.adapters.OverviewOrdersAdapter

class OverviewStaffFragment: Fragment() {

    private lateinit var _binding: StaffFragmentOverviewBinding
    private lateinit var _viewModel: StaffOverviewVM

    private lateinit var _adapterOrders: OverviewOrdersAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _viewModel = ViewModelProvider(this)[StaffOverviewVM::class.java]

        _binding = StaffFragmentOverviewBinding.inflate(inflater, container, false)
        _binding.lifecycleOwner = viewLifecycleOwner
        _binding.viewModel = _viewModel

        val recyclerViewOrders = _binding.recLeftOverview
        _adapterOrders = OverviewOrdersAdapter(_viewModel)
        recyclerViewOrders.adapter = _adapterOrders

        _viewModel.getRealtimeOrdersAll().observe(viewLifecycleOwner) { orders ->
            _adapterOrders.updateData(orders)
        }

        return _binding.root
    }

}