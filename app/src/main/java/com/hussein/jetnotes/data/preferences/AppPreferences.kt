package com.hussein.jetnotes.data.security

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_settings")

class AppPreferences(val context: Context) {
    companion object {
        val PASSCODE_SALT_KEY = stringPreferencesKey("passcode_salt")
        val PASSCODE_HASH_KEY = stringPreferencesKey("passcode_hash")
        val INITIALIZED_KEY = booleanPreferencesKey("initialized")
    }

    suspend fun saveInitialized(initialized: Boolean) {
        context.dataStore.edit { settings ->
            settings[INITIALIZED_KEY] = initialized
        }
    }
    suspend fun savePasscodeCredentials(salt: String, hash: String) {
        context.dataStore.edit { settings ->
            settings[PASSCODE_SALT_KEY] = salt
            settings[PASSCODE_HASH_KEY] = hash
        }
    }

    val getInitialized: Flow<Boolean> = context.dataStore.data.map {
        it[INITIALIZED_KEY] ?: false
    }
    // 3. Flow to read the stored salt
    val getPasscodeSalt: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[PASSCODE_SALT_KEY] }

    // 4. Flow to read the stored hash
    val getPasscodeHash: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[PASSCODE_HASH_KEY] }

}