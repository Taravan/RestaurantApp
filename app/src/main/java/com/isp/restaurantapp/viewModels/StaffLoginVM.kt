package com.isp.restaurantapp.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.isp.restaurantapp.models.Encryption
import com.isp.restaurantapp.models.StaffAccount
import com.isp.restaurantapp.models.exceptions.AccountDoesntExistException
import com.isp.restaurantapp.repositories.RepositoryAbstract
import com.isp.restaurantapp.repositories.RepositoryRetrofit
import kotlinx.coroutines.*

class StaffLoginVM: ViewModel() {
    companion object{
        private const val TAG = "StaffLoginVM"
    }

    private val _repository: RepositoryAbstract
    by lazy{
        RepositoryRetrofit()
    }

    val emailString: MutableLiveData<String> = MutableLiveData()
    val passwdString: MutableLiveData<String> = MutableLiveData()

    private val _errorState: MutableLiveData<String> by lazy{
        MutableLiveData<String>()
    }
    val errorState: LiveData<String>
        get() = _errorState

    private val _errorException: MutableLiveData<Throwable?> by lazy{
        MutableLiveData<Throwable?>()
    }
    val errorException: MutableLiveData<Throwable?>
        get() = _errorException

    private val _isLoading: MutableLiveData<Boolean> by lazy{
        MutableLiveData<Boolean>()
    }
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _staffAccount: MutableLiveData<StaffAccount> by lazy{
        MutableLiveData<StaffAccount>()
    }
    val staffAccount: LiveData<StaffAccount>
        get() = _staffAccount


    fun fetchAccount(){
        val account: String = emailString.value.toString()
        val password: String = passwdString.value.toString()

        val hashedPassword = Encryption.encryptSHA256(password)
        val handler = CoroutineExceptionHandler{ _, throwable ->
            if (throwable is AccountDoesntExistException){
                Log.e(TAG, "fetchAccount: Account doesn't exist")
                _errorException.postValue(throwable)
            }
            throwable.printStackTrace()
        }

        Log.i(TAG, "fetchAccount: hashedPassword passed: $hashedPassword")
        viewModelScope.launch(Dispatchers.IO + handler){
            try {
                val result = _repository.getStaffAccount(account, hashedPassword)
                if (result.body().isNullOrEmpty())
                    throw AccountDoesntExistException()
                withContext(Dispatchers.Main) {
                    Log.i(TAG, "fetchAccount: Proceeding to api call")
                    result.body()?.let {
                        _staffAccount.postValue(it.first())
                    }
                }
            } catch (e: Exception){
                throw e
            }
        }
    }

    fun resetExceptionState(){
        _errorException.postValue(null)
    }
}