package com.isp.restaurantapp.viewModels

import android.security.keystore.UserNotAuthenticatedException
import android.util.Log
import androidx.lifecycle.*
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
import kotlinx.coroutines.withContext

class CustomerActivityVM: ViewModel() {

    companion object{
        const val TAG = "CustomerActivityVM"
    }

    private val _repository: RepositoryAbstract by lazy {
        RepositoryRetrofit()
    }


    private val _table = MutableLiveData<TableDTO>()
    val table: LiveData<TableDTO>
        get() = _table

    private val _tableNumber = MutableLiveData<Int>()
    val tableNumber: LiveData<Int>
        get() = _tableNumber

    // USER AUTH
    private val _auth = FirebaseAuth.getInstance()
    private val _userLogged: MutableLiveData<FirebaseUser> by lazy{
        MutableLiveData<FirebaseUser>(_auth.currentUser)
    }
    val user: MutableLiveData<FirebaseUser>
        get() = _userLogged

    val isUserLoggedIn: LiveData<Boolean>
        get() = _userLogged.map {
            (it == null)
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

    private val _allergenUpdaterService: IAllergenUpdaterService by lazy {
        FrbAllergenUpdaterService()
    }



    // Item is selected via binding in adapter
    // It is there so it is possible get data about the table which was button pressed
    private val _selectedTable = MutableLiveData<TableDTO?>()
    val selectedTable: LiveData<TableDTO?>
        get() = _selectedTable

    private var tables: MutableLiveData<List<TableDTO>> = MutableLiveData<List<TableDTO>>()


    /**
     * Call whenever API response is needed
     * Fills [MutableLiveData] property [tables] with values from database
     */
    fun fetchTablesFromApi(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = _repository.getTables()
                withContext(Dispatchers.Main){
                    tables.value = response
                }
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
                    _listOfAllAllergens.postValue(result)
                    Log.i(TAG, "Loaded all allergens: ${_listOfAllAllergens.value.toString()}")
                }
            }
            catch (e: Exception){
                Log.e(TAG, "Error while getting all allergens", e)
                throw e
            }
        }
    }

    private fun fetchUserDefinedAllergens(uid: String){
        if (uid.isEmpty()){
            throw UserNotAuthenticatedException()
        }
        viewModelScope.launch(Dispatchers.IO){
            try {
                val result = _userDefinedAllergensGetter.getCollection(uid)
                withContext(Dispatchers.Main){
                    _userDefinedAllergens.value = result.toMutableSet()
                    Log.i(TAG, "Loaded user defined allergens: ${_userDefinedAllergens.value.toString()}")
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
            Log.i(TAG, "added: $alg}")
        }
        else{
            _userDefinedAllergens.value?.remove(alg)
            Log.i(TAG, "removed: $alg}")
        }
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
                Log.e(TAG, "Update of user defined allergens failed, ${e.message}")
                throw e
            }
        }
    }

    fun initUserDefinedAllergens(){
        if (isUserLoggedIn.value != true){
            fetchUserDefinedAllergens(_userLogged.value?.uid ?: "")
            Log.w(TAG, "User defined allergens fetched")
        } else {
            Log.e(TAG, "User not logged in: " +
                    "User val= ${_userLogged.value}," +
                    " uid= ${_userLogged.value?.uid}," +
                    " logged in= $isUserLoggedIn")
        }
    }

    init {
        // Just for testing purposes
        _tableNumber.value = 10
    }

}