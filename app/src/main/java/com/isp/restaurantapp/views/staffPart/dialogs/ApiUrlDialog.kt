package com.isp.restaurantapp.views.staffPart.dialogs

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.isp.restaurantapp.databinding.StaffDialogApiUrlBinding
import com.isp.restaurantapp.repositories.ApiSettings
import com.isp.restaurantapp.viewModels.StaffLoginVM
import kotlinx.coroutines.*

class ApiUrlDialog: DialogFragment() {

    companion object{
        private const val TAG = "ApiUrlDialog"
    }

    private lateinit var _binding: StaffDialogApiUrlBinding
    private val _viewModel: StaffLoginVM by viewModels(ownerProducer = {requireParentFragment()})

    private lateinit var _apiSettings: ApiSettings
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = StaffDialogApiUrlBinding.inflate(inflater, container, false)
        //_viewModel = ViewModelProvider(this)[StaffGoodsVM::class.java]
        _binding.lifecycleOwner = viewLifecycleOwner
        _binding.viewModel = _viewModel

        _apiSettings = ApiSettings(_binding.root.context)



        _binding.btnSaveApiUrl.setOnClickListener {
            saveUrlToDataStore()
            dismiss()
        }

        return _binding.root
    }

    private fun saveUrlToDataStore(){
        CoroutineScope(Dispatchers.IO).launch {
            _viewModel.apiUrl.value?.let {
                    value -> _apiSettings.writeApiUrl(value)
                Log.i(TAG, "saveUrlToDataStore: api url written with $value")
            }
        }
    }

}