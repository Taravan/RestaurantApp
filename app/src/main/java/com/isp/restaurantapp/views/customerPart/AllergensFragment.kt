package com.isp.restaurantapp.views.customerPart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.isp.restaurantapp.databinding.FragmentAllergensBinding
import com.isp.restaurantapp.viewModels.AllergensVM
import com.isp.restaurantapp.viewModels.CustomerActivityVM
import com.isp.restaurantapp.views.adapters.AllergenDefinitionAdapterOLD
import com.isp.restaurantapp.views.adapters.TablesBindableAdapter
import com.isp.restaurantapp.views.customerPart.adapters.AllergenDefinitionAdapter

class AllergensFragment: Fragment() {


    private val activityViewModel: CustomerActivityVM by activityViewModels()
    private lateinit var viewModel: AllergensVM
    private lateinit var binding: FragmentAllergensBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAllergensBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[AllergensVM::class.java]

        binding.actVM = activityViewModel

        activityViewModel.fetchAllAllergens()
        activityViewModel.initUserDefinedAllergens()
        activityViewModel.table.observe(viewLifecycleOwner) {
        }

        val allgAdapter = AllergenDefinitionAdapter(activityViewModel)
        binding.recyclerView.adapter = allgAdapter

        activityViewModel.listOfAllAllergens.observe(viewLifecycleOwner) {
            allgAdapter.updateData(it)
        }

        activityViewModel.userDefinedAllergens.observe(viewLifecycleOwner) {

        }


        return binding.root
    }

}