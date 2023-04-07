package com.isp.restaurantapp.viewModels

import android.security.keystore.UserNotAuthenticatedException
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.isp.restaurantapp.models.Allergen
import com.isp.restaurantapp.models.Table
import com.isp.restaurantapp.repositories.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class MainActivityVMOLD: ViewModel() {

    // static const
    companion object val TAG = "MainActivityVM"

    private val retrofitService by lazy {
        RetrofitService()
    }

    // FIREBASE AUTH
    private val _auth = FirebaseAuth.getInstance()
    val auth: FirebaseAuth
        get() = _auth

    private val _userLogged: MutableLiveData<FirebaseUser> by lazy {
        MutableLiveData<FirebaseUser>()
    }
    val loggedUser:  MutableLiveData<FirebaseUser>
        get() = _userLogged

    // Bind this msg to the view, it will be shown if the user is not logged in
    private val _userNotLoggedIn: MutableLiveData<String> by lazy {
        MutableLiveData("User not logged in")
    }
    val userNotLoggedIn: LiveData<String>
        get() = _userNotLoggedIn

    val name: MutableLiveData<String> by lazy {
        MutableLiveData<String>("")
    }

    val email: MutableLiveData<String> by lazy {
        MutableLiveData<String>("nekdo@seznam.cz")
    }

    val password: MutableLiveData<String> by lazy {
        MutableLiveData<String>("password123")
    }


    // ALLERGENS
    // FIREBASE REPO (implements
    private val _allergenGetter: ICollectionGetter<Allergen> by lazy {
        // Can be replaced with dummy class that implements ICollectionGetter
        FrbAllergensGetter()
    }

    private val _listOfAllAllergens: MutableLiveData<List<Allergen>> by lazy {
        MutableLiveData<List<Allergen>>()
    }
    val listOfAllAllergens: LiveData<List<Allergen>>
        get() = _listOfAllAllergens


    // USER DEFINED ALLERGENS
    private val _userDefinedAllergensGetter: ICollectionGetterById<Allergen, String> by lazy {
        FrbUserAllergensGetter()
    }
    val userDefinedAllergensGetter: ICollectionGetterById<Allergen, String>
        get() = _userDefinedAllergensGetter

    private val _userDefinedAllergens: MutableLiveData<MutableSet<Allergen>> by lazy{
        MutableLiveData<MutableSet<Allergen>>()
    }
    val userDefinedAllergens: LiveData<MutableSet<Allergen>>
        get() = _userDefinedAllergens


    // Item is selected via binding in adapter
    // It is there so it is possible get data about the table which was button pressed
    private val _selectedTable = MutableLiveData<Table?>()
    val selectedTable: LiveData<Table?>
        get() = _selectedTable

    private var tables: MutableLiveData<List<Table>> = MutableLiveData<List<Table>>()

    // getter, could be defined as a LiveData property getter as well
    fun getTablesLiveData(): LiveData<List<Table>>{
        return tables
    }



    init {
        checkUserLogIn()
        fetchTablesFromApi()
        fetchAllAllergens()
    }

    private val _allergenUpdaterService: IAllergenUpdaterService by lazy{
        FrbAllergenUpdaterService()
    }
    fun updateAllergensInRepository(){
        viewModelScope.launch(Dispatchers.IO){
            try {
                _userDefinedAllergens.value?.let {
                    _allergenUpdaterService.updateAllergens(
                        _userLogged.value?.uid ?: "none", it.toList()
                    )
                }
            } catch (e: Exception){
                throw e
            }

        }

    }


    private fun checkUserLogIn(){
        _userLogged.value = _auth.currentUser
    }

    /**
     * Call whenever API response is needed
     * Fills [MutableLiveData] property [tables] with values from database
     */
    fun fetchTablesFromApi(){
        viewModelScope.launch {
            try {
                val response = retrofitService.apiService.getTables()
                tables.value = response
            }
            catch (e: Exception) {
                Log.e(TAG, "Error getting tables", e)
            }
        }
    }


    fun fetchAllAllergens(){
        viewModelScope.launch(Dispatchers.IO){
            try{
                val result = _allergenGetter.getCollection()
                withContext(Dispatchers.Main){
                    _listOfAllAllergens.postValue(result.sortedBy { it.id })
                }
            }
            catch (e: Exception){
                Log.e(TAG, "Error while getting all allergens", e)
                throw e
            }
        }
    }

    fun fetchUserDefinedAllergens(uid: String){
        if (uid.isEmpty()){
            throw UserNotAuthenticatedException()
        }
        viewModelScope.launch(Dispatchers.IO){
            try {
                val result = _userDefinedAllergensGetter.getCollection(uid)
                withContext(Dispatchers.Main){
                    _userDefinedAllergens.value = result.toMutableSet()
                }
            } catch (e: Exception){
                Log.e(TAG, "Error while getting user defined allergens", e)
                throw e
            }
        }
    }

    fun isAllergenUserDefined(allergenToExamine: Allergen): Boolean{
        return _userDefinedAllergens.value?.contains(allergenToExamine) ?: false
    }

    fun allergenSwitchAction(isSwitch: Boolean, alg: Allergen) {
        /*
        val currentState = _allergenStatesMap.value.orEmpty()
        val updatedState = currentState + (alg to isSwitch)
        _allergenStatesMap.value = updatedState
        Log.e(TAG, (_allergenStatesMap.value?.get(alg) ?: false).toString())*/
        if (isSwitch){
            _userDefinedAllergens.value?.add(alg)
            Log.e(TAG, "added: $alg}")
        }
        else{
            _userDefinedAllergens.value?.remove(alg)
            Log.e(TAG, "removed: $alg}")
        }


    }
    // This funcs is not required if its put into adapter, not in view
    fun onTableButtonClick(table: Table){
        _selectedTable.value = table
    }

    fun onTableButtonClickComplete(){
        _selectedTable.value = null
    }


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

}