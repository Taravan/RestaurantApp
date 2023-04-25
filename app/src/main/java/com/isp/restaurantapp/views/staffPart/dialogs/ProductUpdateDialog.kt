package com.isp.restaurantapp.views.staffPart.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.isp.restaurantapp.databinding.StaffDialogUpdateProductBinding
import com.isp.restaurantapp.viewModels.StaffGoodsVM
import com.isp.restaurantapp.views.staffPart.adapters.CategoriesSpinnerAdapter
import com.isp.restaurantapp.views.staffPart.adapters.ProductUpdateAllergensAdapter

class ProductUpdateDialog(): DialogFragment() {

    private lateinit var _binding: StaffDialogUpdateProductBinding
    private val _viewModel: StaffGoodsVM by viewModels(ownerProducer = { requireParentFragment() })

    private lateinit var _adapterSpinner: CategoriesSpinnerAdapter
    private lateinit var _adapterAllergens: ProductUpdateAllergensAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = StaffDialogUpdateProductBinding.inflate(inflater, container, false)
        //_viewModel = ViewModelProvider(this)[StaffGoodsVM::class.java]
        _binding.lifecycleOwner = viewLifecycleOwner
        _binding.viewModel = _viewModel

        val categories = _viewModel.categories.value ?: emptyList()
        _adapterSpinner = CategoriesSpinnerAdapter(requireContext(), categories)
        _binding.spinnerCategoryUpdate.adapter = _adapterSpinner

        _adapterAllergens = ProductUpdateAllergensAdapter(_viewModel)
        val recyclerView = _binding.recAllergensUpdate
        recyclerView.adapter = _adapterAllergens

        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _viewModel.allergens.observe(viewLifecycleOwner) { allergens ->
            _adapterAllergens.updateData(allergens)
        }

        _binding.btnUpdateProduct.setOnClickListener {

            _viewModel.updateProduct()
            dismiss()
        }

        _binding.btnCancelProductUpdate.setOnClickListener {
            dismiss()
        }

    }

}