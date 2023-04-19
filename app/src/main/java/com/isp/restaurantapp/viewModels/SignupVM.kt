package com.isp.restaurantapp.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.isp.restaurantapp.models.exceptions.PasswordNotValidException
import com.isp.restaurantapp.models.exceptions.PasswordsDontMatchException
import com.isp.restaurantapp.models.exceptions.UsernameNotValidException

class SignupVM: ViewModel() {
    val emailString: MutableLiveData<String> = MutableLiveData()
    val passwdString: MutableLiveData<String> = MutableLiveData()
    val passwdConfString: MutableLiveData<String> = MutableLiveData()

    private val _emailRegex: Regex by lazy {
        Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
    }

    private val _passwordRegex: Regex by lazy {
        Regex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+\$).{8,}$")
    }

    private val _auth by lazy {
        FirebaseAuth.getInstance()
    }

    /**
     * Creates a user if it matches regex
     */
    fun createNewAccount() {
        val email = emailString.value.orEmpty()
        val passwd = passwdString.value.orEmpty()
        val passwsConf = passwdConfString.value.orEmpty()

        if (!email.matches(_emailRegex)) throw UsernameNotValidException()
        if (!passwd.matches(_passwordRegex)) throw PasswordNotValidException()
        if (passwd != passwsConf) throw PasswordsDontMatchException()

        _auth.createUserWithEmailAndPassword(email, passwd)
    }
}