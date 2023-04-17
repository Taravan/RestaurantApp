package com.isp.restaurantapp.views.customerPart

import android.annotation.SuppressLint
import android.os.Build
import android.os.Build.VERSION
import android.os.Bundle
import android.view.Window
import android.widget.PopupMenu
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
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
import com.google.firebase.FirebaseApp
import com.isp.restaurantapp.R
import com.isp.restaurantapp.databinding.ActivityCustomerBinding
import com.isp.restaurantapp.models.dto.TableDTO
import com.isp.restaurantapp.viewModels.CustomerActivityVM

class CustomerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCustomerBinding
    private lateinit var viewModel: CustomerActivityVM
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityCustomerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[CustomerActivityVM::class.java]

        binding.lifecycleOwner = this
        binding.viewModel = viewModel


        // TODO: Byli testované ty stoly? protože dostávám null, pokud se na ně odkazuji
        //  rovněž nechápu, jak to funguje, kde se ten stůl předává

        /**
         * Must test the android version first to make it
         * compatible with newest and also older versions of android.
         */
        (if (VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("TABLE", TableDTO::class.java)
        } else {
            intent.getParcelableExtra("TABLE")
        })?.let {
            viewModel.setTable(
                it
            )
        }


        FirebaseApp.initializeApp(this)


        val navView: BottomNavigationView = binding.navView

        navController = findNavController(R.id.nav_host_fragment_activity_customer)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_menu, R.id.nav_pay, R.id.profile_navigation
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

    }
}