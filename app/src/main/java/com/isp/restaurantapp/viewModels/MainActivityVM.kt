package com.isp.restaurantapp.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.isp.restaurantapp.models.Table

class MainActivityVM : ViewModel() {

    private val tables = listOf(
        Table(0, 5, "kodStoluCisloPet"),
        Table(1, 10, "kodStoluCisloDeset"),
        Table(2, 15, "kodStoluCisloPatnact")
    )

    private val _navigateToNext = MutableLiveData<Table>()
    val navigateToNext: LiveData<Table>
        get() = _navigateToNext

    private val _tableQr = MutableLiveData<String>()
    val tableQr: LiveData<String>
        get() = _tableQr

    fun onQrScanned(decodedValue: String) {
        val table = tables.find { it.qrCode == decodedValue }
        if (table != null) {
            _navigateToNext.value = table
        }
    }

    fun isValidQrCode(qrCodeValue: String?): Boolean {
        return tables.any {it.qrCode == qrCodeValue}
    }


}