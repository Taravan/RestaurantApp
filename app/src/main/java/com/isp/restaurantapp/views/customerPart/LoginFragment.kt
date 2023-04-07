package com.isp.restaurantapp.views.customerPart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
        savedInstanceState: Bundle?): View {

        val loginVM = ViewModelProvider(this)[LoginVM::class.java]

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        _binding.viewModel = loginVM

        _binding.lifecycleOwner
        val root: View = binding.root


        val textView: TextView = binding.textLogIntoYourAccount
        loginVM.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}