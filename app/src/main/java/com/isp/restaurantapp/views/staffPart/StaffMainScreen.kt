package com.isp.restaurantapp.views.staffPart

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.isp.restaurantapp.R
import com.isp.restaurantapp.viewModels.StaffMainScreenVM

class StaffMainScreen : AppCompatActivity() {



    private lateinit var _viewModel: StaffMainScreenVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_staff_main_screen)

        _viewModel = ViewModelProvider(this)[StaffMainScreenVM::class.java]

    }


}