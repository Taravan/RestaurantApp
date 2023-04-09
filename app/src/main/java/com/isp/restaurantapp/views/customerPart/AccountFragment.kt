package com.isp.restaurantapp.views.customerPart

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.isp.restaurantapp.R
import com.isp.restaurantapp.databinding.FragmentAccountBinding
import com.isp.restaurantapp.viewModels.AccountVM
import com.isp.restaurantapp.views.customerPart.adapters.AccountHolderAdapter

class AccountFragment : Fragment() {

    private lateinit var binding: FragmentAccountBinding
    private lateinit var viewModel: AccountVM

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        viewModel = ViewModelProvider(this)[AccountVM::class.java]
        binding = FragmentAccountBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel


        val pagerAdapter = AccountHolderAdapter(this)
        val pager = binding.viewPagerAccount
        val tabs = binding.tabLayoutAccount
        pager.adapter = pagerAdapter

        TabLayoutMediator(tabs, pager) { tab, position ->
            when (position) {
                0 -> tab.text = "Allergens"
                1 -> tab.text = "History"
            }
        }.attach()


        return binding.root
    }


    // NAVIGATION shown bellow
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogout.setOnClickListener {

            val action = AccountFragmentDirections.actionAccountFragmentToLoginFragment()
            findNavController().navigate(action)

        }

    }

}