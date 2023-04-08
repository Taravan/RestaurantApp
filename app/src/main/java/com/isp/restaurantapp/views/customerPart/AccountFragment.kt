package com.isp.restaurantapp.views.customerPart

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.isp.restaurantapp.R
import com.isp.restaurantapp.databinding.FragmentAccountBinding
import com.isp.restaurantapp.viewModels.AccountVM

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