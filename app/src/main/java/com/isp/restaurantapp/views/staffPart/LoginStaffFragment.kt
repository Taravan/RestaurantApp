package com.isp.restaurantapp.views.staffPart

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.isp.restaurantapp.databinding.StaffFragmentLoginBinding
import com.isp.restaurantapp.viewModels.StaffLoginVM

class LoginStaffFragment: Fragment() {

    private lateinit var _binding: StaffFragmentLoginBinding
    private lateinit var _viewModel: StaffLoginVM

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _viewModel = ViewModelProvider(this)[StaffLoginVM::class.java]

        _binding = StaffFragmentLoginBinding.inflate(inflater, container, false)
        _binding.lifecycleOwner = viewLifecycleOwner
        _binding.viewModel = _viewModel

        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = findNavController()

        _binding.btnStaffLogIn.setOnClickListener {
            val action = LoginStaffFragmentDirections.actionLoginStaffFragmentToTerminalHolderStaffFragment()
            navController.navigate(action)
        }



    }

}