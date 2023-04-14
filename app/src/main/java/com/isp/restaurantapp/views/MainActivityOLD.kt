package com.isp.restaurantapp.views

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.FirebaseApp
import com.isp.restaurantapp.R
import com.isp.restaurantapp.databinding.ActivityMainOldBinding
import com.isp.restaurantapp.viewModels.MainActivityVMOLD
import com.isp.restaurantapp.views.adapters.AllergenDefinitionAdapterOLD
import com.isp.restaurantapp.views.adapters.TablesBindableAdapter

class MainActivityOLD : AppCompatActivity() {

    lateinit var binding: ActivityMainOldBinding
    lateinit var viewModel: MainActivityVMOLD
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)

        // enable data binding and provide view-model
        this.binding = DataBindingUtil.setContentView(this, R.layout.activity_main_old)
        this.viewModel = ViewModelProvider(this)[MainActivityVMOLD::class.java]

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        // create layout manager and adapter for Recyclerview
        val layoutManager = LinearLayoutManager(this)
        val adapter = TablesBindableAdapter(viewModel)

        binding.recyclerViewTables.layoutManager = layoutManager
        binding.recyclerViewTables.adapter = adapter

        // data bind - observe data, set tables as a Recyclerview adapter dataset
        viewModel.getTablesLiveData().observe(this) { tablesDataList ->
            adapter.updateData(tablesDataList)
        }

        // data bind onClickListener
        viewModel.selectedTable.observe(this) { selectedItem ->
            if (selectedItem != null) {
                Toast.makeText(
                    this,
                    "You clicked table number ${selectedItem.tableNumber}",
                    Toast.LENGTH_SHORT
                ).show()
                // Null the selectedItem
                viewModel.onTableButtonClickComplete()
            }
        }

        // once user is logged in, set up/fetch data for rv adapter
        viewModel.loggedUser.observe(this) { user ->
            if (user != null) {
                viewModel.fetchUserDefinedAllergens(viewModel.loggedUser.value?.uid ?: "none")
                val allgAdapter = AllergenDefinitionAdapterOLD(
                    viewModel
                )
                binding.recyclerViewAllergens.adapter = allgAdapter

                viewModel.listOfAllAllergens.observe(this) {
                    allgAdapter.updateData(it)
                }
            }
        }






        binding.btnLogIn.setOnClickListener {
            logIn()
        }

        binding.btnRegister.setOnClickListener {
            registerNewUser()
        }

        binding.btnUpdateAllergens.setOnClickListener {
            updateAllergens()
        }

    }

    fun logIn(){
        try {
            viewModel.logInAndLoadUsersAllergens()
        } catch (e: Exception){
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    fun updateAllergens(){
        try {
            viewModel.updateAllergensInRepository()
        } catch (e: Exception){
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    fun registerNewUser(){
        try {
            viewModel.registerNewUser()
        } catch (e: Exception){
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
        Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show()
    }

}