package com.isp.restaurantapp.views.staffPart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.isp.restaurantapp.databinding.StaffFragmentTablesBinding
import com.isp.restaurantapp.viewModels.StaffMainScreenVM
import com.isp.restaurantapp.viewModels.StaffTablesVM
import com.isp.restaurantapp.views.staffPart.adapters.LeftToPayAdapter
import com.isp.restaurantapp.views.staffPart.adapters.MarkedToPayAdapter
import com.isp.restaurantapp.views.staffPart.adapters.TablesAdapter

class TablesStaffFragment: Fragment() {

    companion object{
        private const val TAG = "TablesStaffFragment"
    }

    private lateinit var _binding: StaffFragmentTablesBinding
    private lateinit var _viewModel: StaffTablesVM
    private val _mainViewModel: StaffMainScreenVM by activityViewModels()

    private lateinit var adapterTables: TablesAdapter
    private lateinit var adapterMarkedToPay: MarkedToPayAdapter
    private lateinit var adapterLeftToPay: LeftToPayAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _viewModel = ViewModelProvider(this)[StaffTablesVM::class.java]

        _binding = StaffFragmentTablesBinding.inflate(inflater, container, false)
        _binding.lifecycleOwner = viewLifecycleOwner
        _binding.viewModel = _viewModel

/*
        _viewModel.errorState.observe(viewLifecycleOwner){
            if (it.isNotEmpty()){
                Toast.makeText(context, R.string.string_error_fetching_tables, Toast.LENGTH_SHORT).show()
                _viewModel.resetErrorState()
            }
        }
*/
        _viewModel.fetchTables()
        //_viewModel.fetchLeftToPay()

        adapterTables = TablesAdapter(_viewModel)
        adapterMarkedToPay = MarkedToPayAdapter(_viewModel)
        adapterLeftToPay = LeftToPayAdapter(_viewModel)

        val recyclerViewTables = _binding.recTableList
        val recyclerViewToPay = _binding.recToPay
        val recyclerViewLeftToPay = _binding.recTableItems

        recyclerViewTables.adapter = adapterTables
        recyclerViewToPay.adapter = adapterMarkedToPay
        recyclerViewLeftToPay.adapter = adapterLeftToPay

        _viewModel.tables.observe(viewLifecycleOwner) { tables ->
            adapterTables.updateData(tables)
        }
/*
        _viewModel.markedToPay.observe(viewLifecycleOwner) { itemsMarkedToPay ->
            adapterMarkedToPay.updateData(itemsMarkedToPay)
        }*/

        _viewModel.selectedTable.observe(viewLifecycleOwner){ selectedTable ->
            _viewModel.getLeftToPay(selectedTable.id).observe(viewLifecycleOwner) {
                adapterLeftToPay.updateData(it)
            }
            _viewModel.getToPay(selectedTable.id).observe(viewLifecycleOwner){
                adapterMarkedToPay.updateData(it)
            }
        }

        _binding.btnPayTerminal.setOnClickListener {
            _mainViewModel.staffAccount.value?.let {
                _viewModel.onPay(it.id)
            }
        }

        return _binding.root
    }

}