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
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.isp.restaurantapp.R
import com.isp.restaurantapp.databinding.StaffFragmentTerminalHolderBinding
import com.isp.restaurantapp.viewModels.StaffTerminalHolderVM

class TerminalHolderStaffFragment: Fragment() {

    private lateinit var _binding: StaffFragmentTerminalHolderBinding
    private lateinit var _viewModel: StaffTerminalHolderVM

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _viewModel = ViewModelProvider(this)[StaffTerminalHolderVM::class.java]

        _binding = StaffFragmentTerminalHolderBinding.inflate(inflater, container, false)
        _binding.lifecycleOwner = viewLifecycleOwner
        _binding.viewModel = _viewModel

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