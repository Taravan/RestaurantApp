package com.isp.restaurantapp.views.staffPart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.isp.restaurantapp.R
import com.isp.restaurantapp.databinding.StaffFragmentOverviewBinding
import com.isp.restaurantapp.models.exceptions.OrderNotPendingDeleteException
import com.isp.restaurantapp.viewModels.StaffOverviewVM
import com.isp.restaurantapp.views.staffPart.adapters.OverviewOrdersAdapter
import com.isp.restaurantapp.views.staffPart.adapters.OverviewPaidOrdersAdapter

class OverviewStaffFragment: Fragment() {

    private lateinit var _binding: StaffFragmentOverviewBinding
    private lateinit var _viewModel: StaffOverviewVM

    private lateinit var _adapterOrders: OverviewOrdersAdapter
    private lateinit var _adapterPaidOrders: OverviewPaidOrdersAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _viewModel = ViewModelProvider(this)[StaffOverviewVM::class.java]

        _viewModel.errorException.observe(viewLifecycleOwner){
            it?.let {
                if (it is OrderNotPendingDeleteException)
                    Toast.makeText(context, getString(R.string.string_non_pending_exception),
                        Toast.LENGTH_SHORT).show()

                _viewModel.resetErrorException()
            }
        }

        _binding = StaffFragmentOverviewBinding.inflate(inflater, container, false)
        _binding.lifecycleOwner = viewLifecycleOwner
        _binding.viewModel = _viewModel

        val recyclerViewOrders = _binding.recLeftOverview
        _adapterOrders = OverviewOrdersAdapter(_viewModel)
        recyclerViewOrders.adapter = _adapterOrders

        val recyclerViewPaidOrders = _binding.recRightOverview
        _adapterPaidOrders = OverviewPaidOrdersAdapter(_viewModel)
        recyclerViewPaidOrders.adapter = _adapterPaidOrders

        _viewModel.getRealtimeOrdersAll().observe(viewLifecycleOwner) { orders ->
            _adapterOrders.updateData(orders)
        }

        _viewModel.getRealtimeOrdersPaid().observe(viewLifecycleOwner) { orders ->
            _adapterPaidOrders.updateData(orders)
        }

        return _binding.root
    }

}