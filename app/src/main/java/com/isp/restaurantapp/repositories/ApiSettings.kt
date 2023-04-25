package com.isp.restaurantapp.repositories

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

class ApiSettings(private val context: Context) {

    companion object{
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
        private val key = stringPreferencesKey("api_url")
    }

    suspend fun getApiKey(): String {
        return context.dataStore.data.first()[key] ?: ""
    }

    suspend fun writeApiUrl(url: String){
        val key = stringPreferencesKey("api_url")
        context.let { context ->
            context.dataStore.edit {
                it[key] = url
            }
        }
    }
}