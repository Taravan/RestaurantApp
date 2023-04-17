package com.isp.restaurantapp.views.staffPart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.isp.restaurantapp.databinding.StaffFragmentGoodsBinding
import com.isp.restaurantapp.viewModels.StaffGoodsVM

class GoodsStaffFragment: Fragment() {

    private lateinit var _binding: StaffFragmentGoodsBinding
    private lateinit var _viewModel: StaffGoodsVM

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _viewModel = ViewModelProvider(this)[StaffGoodsVM::class.java]

        _binding = StaffFragmentGoodsBinding.inflate(inflater, container, false)
        _binding.lifecycleOwner = viewLifecycleOwner
        _binding.viewModel = _viewModel

        return _binding.root
    }

}