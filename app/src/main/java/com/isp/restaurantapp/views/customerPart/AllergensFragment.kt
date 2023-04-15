package com.isp.restaurantapp.views.customerPart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.isp.restaurantapp.databinding.FragmentAllergensBinding
import com.isp.restaurantapp.viewModels.AllergensVM
import com.isp.restaurantapp.viewModels.CustomerActivityVM
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

        initAllergens()

        activityViewModel.table.observe(viewLifecycleOwner) {
        }


        val allgAdapter = AllergenDefinitionAdapter(activityViewModel)

        activityViewModel.listOfAllAllergens.observe(viewLifecycleOwner) {
            allgAdapter.updateData(it)
        }

        // init recyclerview adapter whenever user-defined allergens are ready for main thread
        activityViewModel.userDefinedAllergens.observe(viewLifecycleOwner) {
            binding.recyclerView.adapter = allgAdapter
        }

        binding.buttonSubmit.setOnClickListener { updateAllergens() }

        return binding.root
    }

    private fun initAllergens(){
        activityViewModel.fetchAllAllergens()
        try {
            activityViewModel.initUserDefinedAllergens()
        } catch (e: Exception){
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }
    private fun updateAllergens(){
        try {
            activityViewModel.updateAllergensInRepository()
        } catch (e: Exception){
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }

}