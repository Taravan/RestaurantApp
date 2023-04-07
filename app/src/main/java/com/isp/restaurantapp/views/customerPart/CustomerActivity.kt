package com.isp.restaurantapp.views.customerPart

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Window
import android.widget.PopupMenu
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.isp.restaurantapp.R
import com.isp.restaurantapp.databinding.ActivityCustomerBinding
import com.isp.restaurantapp.viewModels.CustomerActivityVM

class CustomerActivity : AppCompatActivity() {

//    private lateinit var binding: ActivityCustomerBinding

    private lateinit var binding: ActivityCustomerBinding
    private lateinit var viewModel: CustomerActivityVM
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        binding = ActivityCustomerBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        val navView: BottomNavigationView = binding.navView
//
//        val navController = findNavController(R.id.nav_host_fragment_activity_customer)
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//        val appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.nav_menu, R.id.nav_pay, R.id.nav_login
//            )
//        )
//        setupActionBarWithNavController(navController, appBarConfiguration)
//        navView.setupWithNavController(navController)

        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityCustomerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[CustomerActivityVM::class.java]

        binding.lifecycleOwner = this
        binding.viewModel = viewModel




        val navView: BottomNavigationView = binding.navView

        navController = findNavController(R.id.nav_host_fragment_activity_customer)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_menu, R.id.nav_pay, R.id.nav_login
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

    }
}