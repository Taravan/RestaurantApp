package com.isp.restaurantapp.views.staffPart.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.isp.restaurantapp.databinding.StaffDialogNewProductBinding
import com.isp.restaurantapp.models.dto.CategoryDTO
import com.isp.restaurantapp.viewModels.StaffGoodsVM
import com.isp.restaurantapp.views.staffPart.adapters.CategoriesSpinnerAdapter
import com.isp.restaurantapp.views.staffPart.adapters.ProductAddAllergensAdapter

class ProductAddDialog: DialogFragment() {

    private lateinit var _binding: StaffDialogNewProductBinding
    private val _viewModel: StaffGoodsVM by viewModels(ownerProducer = {requireParentFragment()})

    private lateinit var _adapterSpinner: CategoriesSpinnerAdapter
    private lateinit var _adapterAllergens: ProductAddAllergensAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = StaffDialogNewProductBinding.inflate(inflater, container, false)
        //_viewModel = ViewModelProvider(this)[StaffGoodsVM::class.java]
        _binding.lifecycleOwner = viewLifecycleOwner
        _binding.viewModel = _viewModel

        val categories = _viewModel.categories.value ?: emptyList()
        _adapterSpinner = CategoriesSpinnerAdapter(requireContext(), categories)
        _binding.spinnerCategory.adapter = _adapterSpinner

        _adapterAllergens = ProductAddAllergensAdapter(_viewModel)
        val recyclerView = _binding.recAllergens
        recyclerView.adapter = _adapterAllergens

        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        _viewModel.categories.observe(viewLifecycleOwner) { categories ->
//            _adapterSpinner.clear()
//            _adapterSpinner.addAll(categories)
//        }

        _viewModel.allergens.observe(viewLifecycleOwner) { allergens ->
            _adapterAllergens.updateData(allergens)
        }


//        _binding.spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
//                val selectedCategory = p0?.getItemAtPosition(p2) as CategoryDTO
//            }
//            override fun onNothingSelected(p0: AdapterView<*>?) {
//            }
//        }




        _binding.btnCreateProduct.setOnClickListener {

            val name = _binding.etAddProductName.text.toString()
            val desc = _binding.etAddProductDesc.text.toString()
            val cat = _binding.spinnerCategory.selectedItem as CategoryDTO
            val price = _binding.etAddProductPrice.text.toString()

            _viewModel.addProduct(name, desc, cat, price, _viewModel.selectedAllergens.value.orEmpty().toList())
            _viewModel.resetSelectedAllergens()
            dismiss()

        }

        _binding.btnCancelProduct.setOnClickListener {
            _viewModel.resetSelectedAllergens()
            dismiss()
        }

    }

}