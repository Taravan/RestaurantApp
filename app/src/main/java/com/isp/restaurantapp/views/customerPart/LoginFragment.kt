package com.isp.restaurantapp.views.customerPart

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.isp.restaurantapp.databinding.FragmentLoginBinding
import com.isp.restaurantapp.viewModels.LoginVM

class LoginFragment : Fragment() {

    private lateinit var _binding: FragmentLoginBinding

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val viewModel = ViewModelProvider(this)[LoginVM::class.java]

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        _binding.lifecycleOwner = viewLifecycleOwner


        _binding.viewModel = viewModel

        Log.e("view", "logged in as: ${viewModel.loggedUser.value?.email}")

        viewModel.isUserLoggedIn.observe(viewLifecycleOwner) {
                Log.i("view", "Observing isLoggedUser")
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}