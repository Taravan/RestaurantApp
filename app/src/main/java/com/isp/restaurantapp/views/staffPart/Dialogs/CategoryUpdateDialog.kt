package com.isp.restaurantapp.views.staffPart.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.isp.restaurantapp.databinding.StaffDialogUpdateCategoryBinding
import com.isp.restaurantapp.databinding.StaffDialogUpdateTableBinding
import com.isp.restaurantapp.viewModels.StaffGoodsVM

class CategoryUpdateDialog(): DialogFragment() {

    private lateinit var _binding: StaffDialogUpdateCategoryBinding
    private val _viewModel: StaffGoodsVM by viewModels(ownerProducer = { requireParentFragment() })


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = StaffDialogUpdateCategoryBinding.inflate(inflater, container, false)
        //_viewModel = ViewModelProvider(this)[StaffGoodsVM::class.java]
        _binding.lifecycleOwner = viewLifecycleOwner
        _binding.viewModel = _viewModel

        _binding.btnUpdateCategory.setOnClickListener {

            _viewModel.updateCategory()
            dismiss()
        }

        _binding.btnCancelCategoryUpdate.setOnClickListener {
            dismiss()
        }

        return _binding.root
    }

}