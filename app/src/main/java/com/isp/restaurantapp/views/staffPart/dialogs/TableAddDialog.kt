package com.isp.restaurantapp.views.staffPart.dialogs


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.isp.restaurantapp.databinding.StaffDialogNewTableBinding
import com.isp.restaurantapp.viewModels.StaffGoodsVM

class TableAddDialog: DialogFragment() {

    private lateinit var _binding: StaffDialogNewTableBinding
    private val _viewModel: StaffGoodsVM by viewModels(ownerProducer = {requireParentFragment()})


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = StaffDialogNewTableBinding.inflate(inflater, container, false)
        //_viewModel = ViewModelProvider(this)[StaffGoodsVM::class.java]
        _binding.lifecycleOwner = viewLifecycleOwner
        _binding.viewModel = _viewModel

        _binding.btnCreateTable.setOnClickListener {

            val tableNumber = _binding.etAddTableNumber.text.toString()
            val qrCode = _binding.etAddTableQrCode.text.toString()

            _viewModel.addTable(tableNumber, qrCode)

            dismiss()
        }

        _binding.btnCancelTable.setOnClickListener {
            dismiss()
        }

        return _binding.root
    }

//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//
//        _binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.staff_dialog_new_table, null, false)
//        _binding = StaffDialogNewTableBinding.inflate(layoutInflater)
//        _viewModel = ViewModelProvider(this)[StaffGoodsVM::class.java]
//
//        _binding.viewModel = _viewModel
//        _binding.lifecycleOwner = viewLifecycleOwner
//
//            val builder = AlertDialog.Builder(requireContext())
//
//            // Inflate and set the layout for the dialog
//            // Pass null as the parent view because its going in the dialog layout
//            builder.setView(_binding.root)
//                // Add action buttons
//                .setPositiveButton("Add") { _, _ ->
//
//                    val tableNumber = _binding.etAddTableNumber.text.toString()
//                    val qrCode = _binding.etAddTableQrCode.text.toString()
//
//                    _viewModel.addTable(tableNumber, qrCode)
//
//                    dismiss()
//
//                }
//                .setNegativeButton("Cancel") { _, _ ->
//                    dismiss()
//                }
//            return builder.create()
//    }

}