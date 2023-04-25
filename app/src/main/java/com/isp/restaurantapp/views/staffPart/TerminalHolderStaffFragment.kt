package com.isp.restaurantapp.views.staffPart

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.isp.restaurantapp.R
import com.isp.restaurantapp.databinding.StaffFragmentTerminalHolderBinding
import com.isp.restaurantapp.viewModels.StaffMainScreenVM
import com.isp.restaurantapp.viewModels.StaffTerminalHolderVM
import com.isp.restaurantapp.views.staffPart.adapters.PendingOrdersAdapter
import com.isp.restaurantapp.views.staffPart.adapters.ProcessedOrdersAdapter

class TerminalHolderStaffFragment: Fragment() {

    private lateinit var _binding: StaffFragmentTerminalHolderBinding
    private lateinit var _viewModel: StaffTerminalHolderVM
    private val _activityViewModel: StaffMainScreenVM by activityViewModels()

    private lateinit var adapterTop: PendingOrdersAdapter
    private lateinit var adapterBottom: ProcessedOrdersAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _viewModel = ViewModelProvider(this)[StaffTerminalHolderVM::class.java]

        _binding = StaffFragmentTerminalHolderBinding.inflate(inflater, container, false)
        _binding.lifecycleOwner = viewLifecycleOwner
        _binding.viewModel = _viewModel
        _binding.activityVM = _activityViewModel

        _viewModel.errorState.observe(viewLifecycleOwner){
            if (it.isNotEmpty()){
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                _viewModel.resetErrorState()
            }
        }


        adapterTop = PendingOrdersAdapter(_viewModel)
        val recyclerViewTop = _binding.topRecycler
        recyclerViewTop.adapter = adapterTop

        // REALTIME
        _viewModel.getPendingOrders().observe(viewLifecycleOwner){
            adapterTop.updateData(it)
        }

        // REALTIME
        _viewModel.getConfirmedOrders().observe(viewLifecycleOwner){
            adapterBottom.updateData(it)
        }


        adapterBottom = ProcessedOrdersAdapter(_viewModel)
        val recyclerViewBottom = _binding.bottomRecycler
        recyclerViewBottom.adapter = adapterBottom

        _viewModel.processedOrders.observe(viewLifecycleOwner) { processedOrders ->
            adapterBottom.updateData(processedOrders)
        }

        //TODO: Observer on viewModels isLoggedIn and jump to onLogOutRedirect()

        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navHostFragment = childFragmentManager.findFragmentById(R.id.staff_fragment_holder) as NavHostFragment
        val navController = navHostFragment.navController

        _binding.btnOverview.setOnClickListener {
            navController.navigate(R.id.nav_staffOverview)
        }

        _binding.btnTables.setOnClickListener {
            navController.navigate(R.id.nav_staffTables)
        }

        _binding.btnMenuUpdate.setOnClickListener {
            navController.navigate(R.id.nav_staffGoods)
        }

    }

    private fun onLogOutRedirect() {
        val action = TerminalHolderStaffFragmentDirections.actionTerminalHolderStaffFragmentToLoginStaffFragment()
        findNavController().navigate(action)
    }

}