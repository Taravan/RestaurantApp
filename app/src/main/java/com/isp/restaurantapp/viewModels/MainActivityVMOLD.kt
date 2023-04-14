package com.isp.restaurantapp.viewModels

import android.security.keystore.UserNotAuthenticatedException
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.isp.restaurantapp.models.dto.AllergenDTO
import com.isp.restaurantapp.models.dto.TableDTO
import com.isp.restaurantapp.repositories.*
import com.isp.restaurantapp.repositories.concrete.FrbAllergenUpdaterService
import com.isp.restaurantapp.repositories.concrete.FrbAllergensGetter
import com.isp.restaurantapp.repositories.concrete.FrbUserAllergensGetter
import com.isp.restaurantapp.repositories.interfaces.IAllergenUpdaterService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class MainActivityVMOLD: ViewModel() {

    // static const
    companion object val TAG = "MainActivityVM"

    private val repositoryRetrofit by lazy {
        RepositoryRetrofit()
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
    private val _allergenGetter: ICollectionGetter<AllergenDTO> by lazy {
        // Can be replaced with dummy class that implements ICollectionGetter
        FrbAllergensGetter()
    }

    private val _listOfAllAllergens: MutableLiveData<List<AllergenDTO>> by lazy {
        MutableLiveData<List<AllergenDTO>>()
    }
    val listOfAllAllergens: LiveData<List<AllergenDTO>>
        get() = _listOfAllAllergens


    // USER DEFINED ALLERGENS
    private val _userDefinedAllergensGetter: ICollectionGetterById<AllergenDTO, String> by lazy {
        FrbUserAllergensGetter()
    }
    val userDefinedAllergensGetter: ICollectionGetterById<AllergenDTO, String>
        get() = _userDefinedAllergensGetter

    private val _userDefinedAllergens: MutableLiveData<MutableSet<AllergenDTO>> by lazy{
        MutableLiveData<MutableSet<AllergenDTO>>()
    }
    val userDefinedAllergens: LiveData<MutableSet<AllergenDTO>>
        get() = _userDefinedAllergens


    // Item is selected via binding in adapter
    // It is there so it is possible get data about the table which was button pressed
    private val _selectedTable = MutableLiveData<TableDTO?>()
    val selectedTable: LiveData<TableDTO?>
        get() = _selectedTable

    private var tables: MutableLiveData<List<TableDTO>> = MutableLiveData<List<TableDTO>>()

    // getter, could be defined as a LiveData property getter as well
    fun getTablesLiveData(): LiveData<List<TableDTO>>{
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
                val response = repositoryRetrofit.getTables()
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

    fun isAllergenUserDefined(allergenToExamine: AllergenDTO): Boolean{
        return _userDefinedAllergens.value?.contains(allergenToExamine) ?: false
    }

    fun allergenSwitchAction(isSwitch: Boolean, alg: AllergenDTO) {
        if (isSwitch){
            _userDefinedAllergens.value?.add(alg)
            Log.e(TAG, "added: $alg}")
        }
        else{
            _userDefinedAllergens.value?.remove(alg)
            Log.e(TAG, "removed: $alg}")
        }
    }

    // This func is not required if its put into adapter, not in view
    fun onTableButtonClick(table: TableDTO){
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