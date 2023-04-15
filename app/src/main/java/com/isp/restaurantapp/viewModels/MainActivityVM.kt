package com.isp.restaurantapp.viewModels

import android.util.Log
import android.view.MotionEvent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isp.restaurantapp.models.dto.TableDTO
import com.isp.restaurantapp.repositories.RepositoryDataMock
import com.isp.restaurantapp.repositories.interfaces.TableGetterService
import kotlinx.coroutines.launch

class MainActivityVM : ViewModel() {

    companion object {
        const val TAG = "MainActivityVM"
        const val NUMBER_OF_SWIPES = 6
    }

    // Counting continuous swipes of an user
    private var swipeCounter = 0

    private val _tablesRepository: TableGetterService by lazy {
        RepositoryDataMock()
    }

    private var _tables: MutableList<TableDTO> = mutableListOf()
    val tables: MutableList<TableDTO>
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

    private val _navigateToNext = MutableLiveData<TableDTO?>()
    val navigateToNext: LiveData<TableDTO?>
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


    /**
    * =============================================================================================
    *                           ˇˇˇ Redirect to staffs activity ˇˇˇ
    * =============================================================================================
    */
    private val _navigateToStaffScreen = MutableLiveData<Boolean>()
    val navigateToStaffScreen: LiveData<Boolean>
        get() = _navigateToStaffScreen
    fun resetNavigateToStaffScreen() {
        _navigateToStaffScreen.value = false
    }

    fun onFlingRegistered(startPosition: MotionEvent, endPosition: MotionEvent): Boolean {
        if (startPosition.y > endPosition.y) {
            swipeCounter++
            if (swipeCounter >= NUMBER_OF_SWIPES) {
                _navigateToStaffScreen.value = true
                swipeCounter = 0
                return true
            }
        } else {
            swipeCounter = 0
        }
        return false
    }
}