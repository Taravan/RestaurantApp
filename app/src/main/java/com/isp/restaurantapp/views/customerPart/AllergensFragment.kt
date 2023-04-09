package com.isp.restaurantapp.views.customerPart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.isp.restaurantapp.databinding.FragmentAllergensBinding

class AllergensFragment: Fragment() {

    private lateinit var binding: FragmentAllergensBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAllergensBinding.inflate(inflater, container, false)

        return binding.root
    }

}