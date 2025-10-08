package com.hussein.jetnotes.data.security

import android.content.Context
import android.util.Base64
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey

class SecureStorage(context: Context) {

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val dataStore = PreferenceDataStoreFactory.create(
        corruptionHandler = ReplaceFileCorruptionHandler(
            produceNewData = { emptyPreferences() }
        ),
    )

    suspend fun putString(key: String, value: String) {
        val prefsKey = stringPreferencesKey(key)
        dataStore.edit { it[prefsKey] = value }
    }

    suspend fun getString(key: String): String? {
        val prefsKey = stringPreferencesKey(key)
        val prefs = dataStore.data.first()
        return prefs[prefsKey]
    }

    suspend fun putBytes(key: String, value: ByteArray) {
        putString(key, Base64.encodeToString(value, Base64.DEFAULT))
    }

    suspend fun getBytes(key: String): ByteArray? {
        return getString(key)?.let { Base64.decode(it, Base64.DEFAULT) }
    }
}