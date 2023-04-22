package com.isp.restaurantapp.views.staffPart

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.isp.restaurantapp.R
import com.isp.restaurantapp.databinding.StaffFragmentLoginBinding
import com.isp.restaurantapp.models.exceptions.AccountDoesntExistException
import com.isp.restaurantapp.viewModels.StaffLoginVM
import com.isp.restaurantapp.viewModels.StaffMainScreenVM

class LoginStaffFragment: Fragment() {
    companion object{
        private const val TAG = "LoginStaffFragment"
    }

    private val _mainViewModel: StaffMainScreenVM by activityViewModels()
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

        _viewModel.staffAccount.observe(viewLifecycleOwner){
            Log.i(TAG, "onCreateView: logged as $it")
            it?.let {
                _mainViewModel.setStaffAccount(it)
                Log.i(TAG, "onCreateView: account set to main viewmodel, $it")
            }
        }

        _viewModel.errorException.observe(viewLifecycleOwner){
            it?.let {
                if (it is AccountDoesntExistException){
                    Toast.makeText(context, getString(R.string.string_account_not_found), Toast.LENGTH_SHORT)
                        .show()
                    _viewModel.resetExceptionState()
                }
            }
        }

        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = findNavController()


        _viewModel.staffAccount.observe(viewLifecycleOwner){
            it?.let {
                val action = LoginStaffFragmentDirections.actionLoginStaffFragmentToTerminalHolderStaffFragment()
                navController.navigate(action)
            }
        }


        _binding.btnStaffLogIn.setOnClickListener {
            login()
//            val action = LoginStaffFragmentDirections.actionLoginStaffFragmentToTerminalHolderStaffFragment()
//            navController.navigate(action)
        }

    }

    private fun login(){
        try{
            _viewModel.fetchAccount()
        } catch (ea: AccountDoesntExistException){
            Toast.makeText(context, "Account doesnt exist", Toast.LENGTH_SHORT).show()
        } catch (e: java.lang.Exception){
            Log.e(TAG, "login: $e")
        }
    }

}