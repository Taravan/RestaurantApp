package com.isp.restaurantapp.views.customerPart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.isp.restaurantapp.R
import com.isp.restaurantapp.databinding.FragmentSignupBinding
import com.isp.restaurantapp.viewModels.SignupVM


class SignupFragment : Fragment() {

    private lateinit var binding: FragmentSignupBinding
    private lateinit var viewModel: SignupVM

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSignupBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[SignupVM::class.java]
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        return binding.root
    }

    // NAVIGATION shown bellow
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBackToLogin.setOnClickListener {
            backToLogin()
        }

        binding.btnCreateAcc.setOnClickListener {
            // Create acc
            backToLogin()
        }

    }

    private fun backToLogin() {
        val action = SignupFragmentDirections.actionSignupFragmentToLoginFragment()
        findNavController().navigate(action)
    }

}