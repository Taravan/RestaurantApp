package com.isp.restaurantapp.views.staffPart.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.isp.restaurantapp.databinding.StaffDialogApiUrlBinding
import com.isp.restaurantapp.databinding.StaffDialogNewCategoryBinding
import com.isp.restaurantapp.viewModels.StaffGoodsVM
import com.isp.restaurantapp.viewModels.StaffLoginVM

class ApiUrlDialog: DialogFragment() {

    private lateinit var _binding: StaffDialogApiUrlBinding
    private val _viewModel: StaffLoginVM by viewModels(ownerProducer = {requireParentFragment()})

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = StaffDialogApiUrlBinding.inflate(inflater, container, false)
        //_viewModel = ViewModelProvider(this)[StaffGoodsVM::class.java]
        _binding.lifecycleOwner = viewLifecycleOwner
        _binding.viewModel = _viewModel

        _binding.btnSaveApiUrl.setOnClickListener {

            _viewModel.onSaveApiUrl()
            dismiss()
        }

        return _binding.root
    }

}