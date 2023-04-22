package com.isp.restaurantapp.views.staffPart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.isp.restaurantapp.databinding.StaffFragmentGoodsBinding
import com.isp.restaurantapp.models.dto.TableDTO
import com.isp.restaurantapp.viewModels.StaffGoodsVM
import com.isp.restaurantapp.views.staffPart.Dialogs.CategoryAddDialog
import com.isp.restaurantapp.views.staffPart.Dialogs.ProductAddDialog
import com.isp.restaurantapp.views.staffPart.Dialogs.TableAddDialog
import com.isp.restaurantapp.views.staffPart.adapters.CategoriesOverviewAdapter
import com.isp.restaurantapp.views.staffPart.adapters.MenuOverviewAdapter
import com.isp.restaurantapp.views.staffPart.adapters.TablesOverviewAdapter

class GoodsStaffFragment: Fragment() {

    private lateinit var _binding: StaffFragmentGoodsBinding
    private lateinit var _viewModel: StaffGoodsVM

    private lateinit var _adapterTables: TablesOverviewAdapter
    private lateinit var _adapterCategories: CategoriesOverviewAdapter
    private lateinit var _adapterMenu: MenuOverviewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _viewModel = ViewModelProvider(this)[StaffGoodsVM::class.java]

        _binding = StaffFragmentGoodsBinding.inflate(inflater, container, false)
        _binding.lifecycleOwner = viewLifecycleOwner
        _binding.viewModel = _viewModel

        _viewModel.fetchTables()
//        _viewModel.fetchCategories()
        _viewModel.fetchGoods()

        _adapterTables = TablesOverviewAdapter(_viewModel)
        _adapterCategories = CategoriesOverviewAdapter(_viewModel)
        _adapterMenu = MenuOverviewAdapter(_viewModel)

        val recyclerViewTables = _binding.recTablesOverview
        val recyclerViewCategories = _binding.recCategoriesOverview
        val recyclerViewMenu = _binding.recMenuOverview

        recyclerViewTables.adapter = _adapterTables
        recyclerViewCategories.adapter = _adapterCategories
        recyclerViewMenu.adapter = _adapterMenu

        _viewModel.tables.observe(viewLifecycleOwner) { tables ->
            _adapterTables.updateData(tables)
        }

        _viewModel.categories.observe(viewLifecycleOwner) { categories ->
            _adapterCategories.updateData(categories)
        }

        _viewModel.goods.observe(viewLifecycleOwner) { goods ->
            _adapterMenu.updateData(goods)
        }

        _binding.btnNewTable.setOnClickListener {
            openNewTableDialog()
        }

        _binding.btnNewCategory.setOnClickListener {
            openNewCategoryDialog()
        }

        _binding.btnNewProduct.setOnClickListener {
            openNewProductDialog()
        }

        return _binding.root
    }

    private fun openNewTableDialog() {

        val dialog = TableAddDialog()
        dialog.show(parentFragmentManager, "AddTable")

    }

    private fun openNewCategoryDialog() {

        val dialog = CategoryAddDialog()
        dialog.show(parentFragmentManager, "AddCategory")

    }

    private fun openNewProductDialog() {

        val dialog = ProductAddDialog()
        dialog.show(parentFragmentManager, "AddProduct")

    }

}