package com.isp.restaurantapp.views.staffPart.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.isp.restaurantapp.databinding.StaffDialogUpdateTableBinding
import com.isp.restaurantapp.viewModels.StaffGoodsVM

class TableUpdateDialog(): DialogFragment() {

    private lateinit var _binding: StaffDialogUpdateTableBinding
    private val _viewModel: StaffGoodsVM by viewModels(ownerProducer = { requireParentFragment() })


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = StaffDialogUpdateTableBinding.inflate(inflater, container, false)
        //_viewModel = ViewModelProvider(this)[StaffGoodsVM::class.java]
        _binding.lifecycleOwner = viewLifecycleOwner
        _binding.viewModel = _viewModel

        _binding.btnUpdateTable.setOnClickListener {

            _viewModel.updateTable()
            dismiss()
        }

        _binding.btnCancelUpdateTable.setOnClickListener {
            dismiss()
        }

        return _binding.root
    }

}