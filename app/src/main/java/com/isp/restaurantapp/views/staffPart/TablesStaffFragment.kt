package com.isp.restaurantapp.views.staffPart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.isp.restaurantapp.databinding.StaffFragmentTablesBinding
import com.isp.restaurantapp.viewModels.StaffTablesVM

class TablesStaffFragment: Fragment() {

    private lateinit var _binding: StaffFragmentTablesBinding
    private lateinit var _viewModel: StaffTablesVM

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _viewModel = ViewModelProvider(this)[StaffTablesVM::class.java]

        _binding = StaffFragmentTablesBinding.inflate(inflater, container, false)
        _binding.lifecycleOwner = viewLifecycleOwner
        _binding.viewModel = _viewModel

        return _binding.root
    }

}