package com.isp.restaurantapp.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class LoginVM : ViewModel() {

    companion object val TAG = "LoginVM"

    // FIREBASE AUTH
    private val _auth = FirebaseAuth.getInstance()
    val auth: FirebaseAuth
        get() = _auth

    private val _userLogged: MutableLiveData<FirebaseUser> by lazy {
        MutableLiveData<FirebaseUser>()
    }
    val loggedUser:  MutableLiveData<FirebaseUser>
        get() = _userLogged

    val name: MutableLiveData<String> by lazy {
        MutableLiveData<String>("")
    }

    val email: MutableLiveData<String> by lazy {
        MutableLiveData<String>("nekdo@seznam.cz")
    }

    val password: MutableLiveData<String> by lazy {
        MutableLiveData<String>("password123")
    }

    private val _text = MutableLiveData<String>().apply {
        value = "This is log in Fragment"
    }
    val text: LiveData<String> = _text


    fun registerNewUser(){
        if (email.value.isNullOrEmpty() || password.value.isNullOrEmpty()){
            throw Exception("Email ani heslo nesmí zůstat prázdné")
        }

        viewModelScope.launch {
            try {
                val response =_auth.createUserWithEmailAndPassword(
                    email.value.orEmpty(),
                    password.value.orEmpty()
                ).await()  // block until done
                _userLogged.value = response.user
            } catch (e: Exception){
                Log.e(TAG, "Error with registering user: ", e)
                throw e
            }
        }
    }

    fun logInAndLoadUsersAllergens() {
        Log.e(TAG, "dopiči vole")
        if (email.value.isNullOrEmpty() || password.value.isNullOrEmpty()){
            throw Exception("Email ani heslo nesmí zůstat prázdné")
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response =_auth.signInWithEmailAndPassword(
                    email.value.orEmpty(),
                    password.value.orEmpty()
                ).await()
                withContext(Dispatchers.Main){
                    _userLogged.value = response.user
                }

            } catch (e: Exception){
                Log.e(TAG, "Error with user log in: ", e)
            }
        }
    }
    fun logOut() {

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response =_auth.signOut()
                withContext(Dispatchers.Main){
                    _userLogged.value = null
                    Log.e(TAG, "null now: ${_userLogged.value}")
                }

            } catch (e: Exception){
                Log.e(TAG, "Error with user log in: ", e)
            }
        }
    }
}