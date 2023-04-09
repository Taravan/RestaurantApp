package com.isp.restaurantapp.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isp.restaurantapp.models.Table
import com.isp.restaurantapp.repositories.DataMock
import com.isp.restaurantapp.repositories.interfaces.TableGetterService
import kotlinx.coroutines.launch

class MainActivityVM : ViewModel() {

    companion object {const val TAG = "MainActivityVM"}

    private val _tablesRepository: TableGetterService by lazy {
        DataMock()
    }

    private var _tables: MutableList<Table> = mutableListOf()
    val tables: MutableList<Table>
        get() = _tables


    fun fetchTables(){
        viewModelScope.launch {
            try {
                _tables = _tablesRepository.getTables().toMutableList()
            } catch (e: Exception){
                Log.e(TAG, e.message.toString())
                throw e
            }
        }
    }

    private val _navigateToNext = MutableLiveData<Table?>()
    val navigateToNext: LiveData<Table?>
        get() = _navigateToNext

    private val _tableQr = MutableLiveData<String>()
    val tableQr: LiveData<String>
        get() = _tableQr

    fun onQrScanned(decodedValue: String) {
        if (_tables.size < 1) fetchTables()

        val table = _tables.find { it.qrCode == decodedValue }
        if (table != null) {
            _navigateToNext.postValue(table)
        }
    }

    fun isValidQrCode(qrCodeValue: String?): Boolean {
        return _tables.any {it.qrCode == qrCodeValue}
    }


}