package com.isp.restaurantapp.views.customerPart

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.isp.restaurantapp.R
import com.isp.restaurantapp.views.customerPart.adapters.MenuHolderAdapter
import com.isp.restaurantapp.databinding.FragmentMenuHolderBinding
import com.isp.restaurantapp.viewModels.CustomerActivityVM
import com.isp.restaurantapp.viewModels.MenuHolderVM

class MenuHolderFragment : Fragment() {
    companion object{
        const val TAG = "MenuHolderFragment"
    }

    private val activityViewModel: CustomerActivityVM by activityViewModels()
    private lateinit var viewModel: MenuHolderVM
    private lateinit var binding: FragmentMenuHolderBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_menu_holder, container, false)
        viewModel = ViewModelProvider(this)[MenuHolderVM::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner


        // FETCH & FETCH DATA, this should not be in init() method of VM
        // You have more control about whats happening
        viewModel.fetchData()
        initUserAllergens()


        activityViewModel.isUserLoggedIn.observe(viewLifecycleOwner){
        }



        val adapter = MenuHolderAdapter(viewModel)
        binding.viewPagerMenu.adapter = adapter

        var tabLayoutMediator: TabLayoutMediator? = null

        viewModel.goodsAllergens.observe(viewLifecycleOwner){
            Log.i(TAG, it.toString())
        }
        viewModel.userDefinedAllergens.observe(viewLifecycleOwner){
            Log.i(TAG, it.toString())
        }
        viewModel.userDefinedAllergensString.observe(viewLifecycleOwner){
            Log.i(TAG,"User allergen string: $it")
        }



        // Once all necessary data has been fetched and published, prepare the UI
        viewModel.isAllDataFetched.observe(viewLifecycleOwner) {
            // Once the data is fetched by IO and then confirmed and exposed by Dispatchers.Main
            if (it == true){
                Log.i(TAG, "Dataset ready, initiating adapter & mediator update")
                viewModel.menuCategories.observe(viewLifecycleOwner) {
                    // Once the data is fetched by IO and then confirmed and exposed by Dispatchers.Main
                    if (!it.isNullOrEmpty()){
                        Log.i(TAG, "Dataset ready, initiating adapter & mediator update")
                        adapter.updateData(it)

                        tabLayoutMediator?.detach()
                        tabLayoutMediator = TabLayoutMediator(binding.tabLayoutMenu, binding.viewPagerMenu) { tab, position ->
                            tab.text = it[position].categoryName
                        }

                        tabLayoutMediator?.attach()
                    }
                }
            }
        }



        return binding.root
    }

    private fun initUserAllergens(){
        try {
            viewModel.fetchUserDefinedAllergens(activityViewModel.user.value?.uid ?: "")
        } catch (e: Exception){
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }
}