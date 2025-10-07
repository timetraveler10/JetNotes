package com.hussein.jetnotes.data.security

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

class CipherHelper(val context: Context) {
    val keyStore = KeyStore.getInstance("AndroidKeyStore").apply {
        load(null)
    }
    private val keyAlias = "com.hussein.jetnotes.encryption_key"


    fun getOrGenerateSecretKey(): SecretKey {
        // 1. USE THE ALIAS to check if the key already exists in the KeyStore
        if (keyStore.containsAlias(keyAlias)) {
            // Retrieve the key using its alias
            val entry = keyStore.getEntry(keyAlias, null) as KeyStore.SecretKeyEntry
            return entry.secretKey
        }

        // 2. USE THE ALIAS to create a new key if it doesn't exist
        val keyGenParams = KeyGenParameterSpec.Builder(
            keyAlias, // The alias is the first and most important parameter
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        ).apply {
            setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            setKeySize(256)
            setUserAuthenticationRequired(true)
            setUserAuthenticationParameters(
                0,  // timeout = 0 → require biometric every use
                KeyProperties.AUTH_BIOMETRIC_STRONG or KeyProperties.AUTH_DEVICE_CREDENTIAL
            )
        }.build()

        val keyGenerator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES,
            "AndroidKeyStore"
        )
        keyGenerator.init(keyGenParams)

        // This generates the key and stores it in the KeyStore under your chosen alias
        return keyGenerator.generateKey()
    }


    fun encrypt(plainText: String, cipher: Cipher): String {
        val encryptedBytes = cipher.doFinal(plainText.toByteArray())
        val iv = cipher.iv
        // Prepend IV to the encrypted data for use during decryption
        return Base64.encodeToString(iv + encryptedBytes, Base64.NO_WRAP)
    }

    fun decrypt(cipherText: String, cipher: Cipher): String {
        val decodedBytes = Base64.decode(cipherText, Base64.NO_WRAP)
        // The IV was prepended during encryption, so we don't need to extract it
        // The Cipher is already initialized with the correct IV by the biometric prompt
        val decryptedBytes = cipher.doFinal(decodedBytes)
        return String(decryptedBytes, Charsets.UTF_8)
    }

    fun getDecryptionCipher(cipherText: String): Cipher {
        val decodedBytes = Base64.decode(cipherText, Base64.NO_WRAP)
        val iv = decodedBytes.copyOfRange(0, 12) // GCM IV is 12 bytes
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val key = getOrGenerateSecretKey()
        // Initialize the cipher for decryption with the extracted IV
        cipher.init(Cipher.DECRYPT_MODE, key, GCMParameterSpec(128, iv))
        return cipher
    }

    fun getEncryptionCipher(): Cipher {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val key = getOrGenerateSecretKey()
        cipher.init(Cipher.ENCRYPT_MODE, key)
        return cipher
    }
}