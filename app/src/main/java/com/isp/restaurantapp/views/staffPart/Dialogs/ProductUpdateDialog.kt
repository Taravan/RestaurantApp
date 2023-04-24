package com.isp.restaurantapp.views.staffPart.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.isp.restaurantapp.databinding.StaffDialogUpdateProductBinding
import com.isp.restaurantapp.viewModels.StaffGoodsVM

class ProductUpdateDialog(): DialogFragment() {

    private lateinit var _binding: StaffDialogUpdateProductBinding
    private val _viewModel: StaffGoodsVM by viewModels(ownerProducer = { requireParentFragment() })


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = StaffDialogUpdateProductBinding.inflate(inflater, container, false)
        //_viewModel = ViewModelProvider(this)[StaffGoodsVM::class.java]
        _binding.lifecycleOwner = viewLifecycleOwner
        _binding.viewModel = _viewModel

        _binding.btnUpdateProduct.setOnClickListener {

            _viewModel.updateProduct()
            dismiss()
        }

        _binding.btnCancelProductUpdate.setOnClickListener {
            dismiss()
        }

        return _binding.root
    }

}