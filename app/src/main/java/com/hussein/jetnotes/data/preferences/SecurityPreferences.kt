package com.hussein.jetnotes.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.securityDatastore: DataStore<Preferences> by preferencesDataStore(name = "security_settings")

class SecurityPreferences(val context: Context) {
    companion object {
        val PASSCODE_SALT_KEY = stringPreferencesKey("passcode_salt")
        val PASSCODE_HASH_KEY = stringPreferencesKey("passcode_hash")
    }
    suspend fun savePasscodeCredentials(salt: String, hash: String) {
        context.securityDatastore.edit { settings ->
            settings[PASSCODE_SALT_KEY] = salt
            settings[PASSCODE_HASH_KEY] = hash
        }
    }
    val getPasscodeSalt: Flow<String?> = context.securityDatastore.data
        .map { preferences -> preferences[PASSCODE_SALT_KEY] }

    // 4. Flow to read the stored hash
    val getPasscodeHash: Flow<String?> = context.securityDatastore.data
        .map { preferences -> preferences[PASSCODE_HASH_KEY] }
}