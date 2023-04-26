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
import com.isp.restaurantapp.repositories.ApiSettings
import com.isp.restaurantapp.models.exceptions.AccountDoesntExistException
import com.isp.restaurantapp.models.exceptions.RetrofitFailedException
import com.isp.restaurantapp.viewModels.StaffLoginVM
import com.isp.restaurantapp.viewModels.StaffMainScreenVM
import com.isp.restaurantapp.views.staffPart.dialogs.ApiUrlDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginStaffFragment: Fragment() {
    companion object{
        private const val TAG = "LoginStaffFragment"
    }

    private val _mainViewModel: StaffMainScreenVM by activityViewModels()

    private lateinit var _apiSettings: ApiSettings
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


        // LOAD DATASTORE
        _apiSettings = ApiSettings(_binding.root.context)
        loadUrlFromDatastore()

        _viewModel.staffAccount.observe(viewLifecycleOwner){
            Log.i(TAG, "onCreateView: logged as $it")
            it?.let {
                _mainViewModel.setStaffAccount(it)
                Log.i(TAG, "onCreateView: account set to main viewmodel, $it")
            }
        }

        // Expose encoded api url to whole project
        _viewModel.apiUrl.observe(viewLifecycleOwner){
            _mainViewModel.setEncodedApiUrl(it)
        }

        /**
         * Error handling, show error states to the user
         */
        _viewModel.errorException.observe(viewLifecycleOwner){
            it?.let {
                when (it){
                    is AccountDoesntExistException -> {
                        Toast.makeText(context, getString(R.string.string_account_not_found), Toast.LENGTH_SHORT)
                            .show()
                    }
                    is RetrofitFailedException -> {
                        Toast.makeText(context, getString(R.string.string_bad_url_format), Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        Toast.makeText(context, getString(R.string.string_bad_url_timeout), Toast.LENGTH_LONG).show()
                    }
                }
                _viewModel.resetExceptionState()
            }
        }

        _binding.btnSetApiUrl.setOnClickListener {
            openApiUrlDialog()
        }

        return _binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = findNavController()


        _mainViewModel.staffAccount.observe(viewLifecycleOwner){
            it?.let {
                val action = LoginStaffFragmentDirections.actionLoginStaffFragmentToTerminalHolderStaffFragment()
                navController.navigate(action)
            }
        }


        _binding.btnStaffLogIn.setOnClickListener {
            login()
        }

    }

    private fun login(){
        try{
            _viewModel.fetchAccount()
        } catch (ea: AccountDoesntExistException){
            Toast.makeText(context, "Account does not exist", Toast.LENGTH_SHORT).show()
        } catch (e: java.lang.Exception){
            Log.e(TAG, "login: $e")
        }
    }

    private fun loadUrlFromDatastore() {
        CoroutineScope(Dispatchers.IO).launch{
            val apiRead: String = _apiSettings.getApiKey()
            withContext(Dispatchers.Main){
                _viewModel.apiUrl.postValue(apiRead)
                Log.e(TAG, "LoadUrlFromDataStore: apiUrl loaded from datastore with $apiRead")
            }
        }
    }

    private fun openApiUrlDialog() {
        val dialog = ApiUrlDialog()
        dialog.show(childFragmentManager, "SetApiUrl")
    }


//    private fun saveUrlToDataStore(){
//        CoroutineScope(Dispatchers.IO).launch {
//            _viewModel.apiUrl.value?.let {
//                    value -> _apiSettings.writeApiUrl(value)
//                Log.i(TAG, "saveUrlToDataStore: api url written with $value")
//            }
//        }
//    }
}


