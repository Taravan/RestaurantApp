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

        /* If a user is not logged in, navigate him to login fragment
         * Button with logOut only log out the user
         * Because of this observe the nav action happens automatically
         */
        viewModel.isUserLoggedIn.observe(viewLifecycleOwner) {
            if(!it){
                // User is not logged in, navigate him to login fragment
                val action = AccountFragmentDirections.actionAccountFragmentToLoginFragment()
                findNavController().navigate(action)
            }
        }

        binding.btnLogout.setOnClickListener {
            val action = AccountFragmentDirections.actionAccountFragmentToLoginFragment()
            findNavController().navigate(action)
        }

    }

}