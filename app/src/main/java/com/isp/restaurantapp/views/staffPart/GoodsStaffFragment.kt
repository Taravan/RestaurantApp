package com.isp.restaurantapp.views.staffPart

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.isp.restaurantapp.databinding.StaffFragmentGoodsBinding
import com.isp.restaurantapp.models.dto.CategoryDTO
import com.isp.restaurantapp.models.dto.GoodsItemDTO
import com.isp.restaurantapp.models.dto.TableDTO
import com.isp.restaurantapp.viewModels.StaffGoodsVM
import com.isp.restaurantapp.viewModels.StaffMainScreenVM
import com.isp.restaurantapp.views.staffPart.adapters.CategoriesOverviewAdapter
import com.isp.restaurantapp.views.staffPart.adapters.MenuOverviewAdapter
import com.isp.restaurantapp.views.staffPart.adapters.TablesOverviewAdapter
import com.isp.restaurantapp.views.staffPart.dialogs.*

class GoodsStaffFragment: Fragment(), TablesOverviewAdapter.Callback, CategoriesOverviewAdapter.Callback, MenuOverviewAdapter.Callback {

    companion object{
        private const val TAG = "GoodsStaffFragment"
    }

    private lateinit var _binding: StaffFragmentGoodsBinding
    private lateinit var _viewModel: StaffGoodsVM

    private lateinit var _adapterTables: TablesOverviewAdapter
    private lateinit var _adapterCategories: CategoriesOverviewAdapter
    private lateinit var _adapterMenu: MenuOverviewAdapter

    private val _activityViewModel: StaffMainScreenVM by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _viewModel = ViewModelProvider(this)[StaffGoodsVM::class.java]

        _binding = StaffFragmentGoodsBinding.inflate(inflater, container, false)
        _binding.lifecycleOwner = viewLifecycleOwner
        _binding.viewModel = _viewModel

        _activityViewModel.encodedApiUrl.observe(viewLifecycleOwner){
            _viewModel.prefix = it
            Log.i(TAG, "onCreateView: set up prefix to view-model $it")
        }

        _viewModel.fetchTables()
        _viewModel.fetchCategories()
        _viewModel.fetchGoods()
        _viewModel.fetchAllergens()

        _adapterTables = TablesOverviewAdapter(_viewModel, this)
        _adapterCategories = CategoriesOverviewAdapter(_viewModel, this)
        _adapterMenu = MenuOverviewAdapter(_viewModel, this)

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


    /**
     * Table dialogs
     */
    private fun openNewTableDialog() {
        val dialog = TableAddDialog()
        dialog.show(childFragmentManager, "AddTable")
    }

    override fun updateTableDialog(table: TableDTO) {
        _viewModel.setUpdatedTable(table)
        val dialog = TableUpdateDialog()
        dialog.show(childFragmentManager, "UpdateTable")
    }

    /**
     * Category create dialog
     */
    private fun openNewCategoryDialog() {
        val dialog = CategoryAddDialog()
        dialog.show(childFragmentManager, "AddCategory")
    }

    override fun updateCategoryDialog(category: CategoryDTO) {
        _viewModel.setUpdatedCategory(category)
        val dialog = CategoryUpdateDialog()
        dialog.show(childFragmentManager, "UpdateCategory")
    }

    /**
     * Product create dialog
     */
    private fun openNewProductDialog() {
        val dialog = ProductAddDialog()
        dialog.show(childFragmentManager, "AddProduct")
    }

    override fun updateProductDialog(product: GoodsItemDTO) {
        _viewModel.setUpdatedProduct(product)
        val dialog = ProductUpdateDialog()
        dialog.show(childFragmentManager, "UpdateProduct")
    }

}