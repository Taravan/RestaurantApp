package com.isp.restaurantapp.views.customerPart

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.isp.restaurantapp.R
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


    /*
    * NAVIGATION
    * Better to have it in viewcreated after fragment inflates.
    * All navigation is already set up.
    * Back stack is already handled in navigation so no need to handle it here.
    * Only one redirection needs to be handled just from this class shown in onResume().
    * Basic example without any rules shown bellow.
    */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = findNavController()

        // U can bind it to layout again and give it rules in VM
        binding.btnLogIn.setOnClickListener {
            // Login process
            val action = LoginFragmentDirections.actionLoginFragmentToAccountFragment()
            navController.navigate(action)
        }

        binding.btnSignUp.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToSignupFragment()
            navController.navigate(action)
        }

    }


    /*
    * Live data must be observed and redirection
    * called if user is logged in.
    * Redirection is just in this home class
    */
    override fun onResume() {
        super.onResume()
        //if ( logged In ) {
        //    findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToAccountFragment())
        //}
    }

}