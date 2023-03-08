package com.isp.restaurantapp.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.isp.restaurantapp.R
import com.isp.restaurantapp.databinding.ActivityMainBinding
import com.isp.restaurantapp.viewModels.MainActivityVM

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val viewModel = ViewModelProvider(this)[MainActivityVM::class.java]

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }
}