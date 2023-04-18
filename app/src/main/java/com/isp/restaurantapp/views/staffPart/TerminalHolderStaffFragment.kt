package com.isp.restaurantapp.views.staffPart

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.isp.restaurantapp.R
import com.isp.restaurantapp.databinding.StaffFragmentTerminalHolderBinding
import com.isp.restaurantapp.viewModels.StaffTerminalHolderVM
import com.isp.restaurantapp.views.customerPart.adapters.PayAdapter
import com.isp.restaurantapp.views.staffPart.adapters.PendingOrdersAdapter
import com.isp.restaurantapp.views.staffPart.adapters.ProcessedOrdersAdapter

class TerminalHolderStaffFragment: Fragment() {

    private lateinit var _binding: StaffFragmentTerminalHolderBinding
    private lateinit var _viewModel: StaffTerminalHolderVM

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

//
//        _viewModel.fetchPendingOrders()
//        _viewModel.fetchProcessedOrders()
        adapterTop = PendingOrdersAdapter(_viewModel)
        val recyclerViewTop = _binding.topRecycler
        recyclerViewTop.adapter = adapterTop

        _viewModel.getPendingOrders().observe(viewLifecycleOwner){
            adapterTop.updateData(it)
        }

        _viewModel.getConfirmedOrders().observe(viewLifecycleOwner){
            adapterBottom.updateData(it)
        }

//        _viewModel.pendingOrders.observe(viewLifecycleOwner) { pendingOrders ->
//            adapterTop.updateData(pendingOrders)
//        }

        adapterBottom = ProcessedOrdersAdapter(_viewModel)
        val recyclerViewBottom = _binding.bottomRecycler
        recyclerViewBottom.adapter = adapterBottom

        _viewModel.processedOrders.observe(viewLifecycleOwner) { processedOrders ->
            adapterBottom.updateData(processedOrders)
        }

        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navHostFragment = childFragmentManager.findFragmentById(R.id.staff_fragment_holder) as NavHostFragment
        val navController = navHostFragment.navController
        Log.e("fff", navController.currentDestination.toString())

        _binding.btnOverview.setOnClickListener {
            navController.navigate(R.id.nav_staffOverview)
        }

        _binding.btnTables.setOnClickListener {
            navController.navigate(R.id.nav_staffTables)
            //val action = OverviewStaffFragmentDirections.actionNavStaffOverviewToNavStaffTables()
            //navController.navigate(action)
        }

        _binding.btnMenuUpdate.setOnClickListener {
            navController.navigate(R.id.nav_staffGoods)
        }



    }

}