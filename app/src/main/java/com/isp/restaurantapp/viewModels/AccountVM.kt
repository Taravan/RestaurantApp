package com.isp.restaurantapp.viewModels

import android.util.Log
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AccountVM : ViewModel() {
    companion object{
        const val TAG = "AccountVM"
    }

    // FIREBASE AUTH
    private val _auth = FirebaseAuth.getInstance()

    private val _userLogged: MutableLiveData<FirebaseUser> by lazy {
        MutableLiveData<FirebaseUser>(_auth.currentUser)
    }
    val loggedUser: LiveData<String> = _userLogged.map {
        // needs to be checked cause signout process nulls
        // and ui still attempts to read/map it
        if (it !=null)
            it.email?: "Anonymous"
        else ""// if its null, just map&read the empty string
    }
    val isUserLoggedIn: LiveData<Boolean> = _userLogged.map { (it != null) }

    fun logOut() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response =_auth.signOut()
                withContext(Dispatchers.Main){
                    _userLogged.postValue(null)
                }

            } catch (e: Exception){
                Log.e(TAG, "Error with user log in: ", e)
            }
        }
    }
}