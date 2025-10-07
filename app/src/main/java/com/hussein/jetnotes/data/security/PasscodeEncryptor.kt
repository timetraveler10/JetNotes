package com.hussein.jetnotes.data.security

import android.util.Base64
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

object PasscodeEncryptor {
    private const val ITERATION_COUNT = 65536
    private const val KEY_LENGTH = 256
    private const val AES_MODE = "AES/GCM/NoPadding"
// the salt should be saved alongside the hashed passcode
    private fun deriveKeyFromPasscode(
        passCode: String,
        salt: ByteArray
    ): SecretKeySpec {

        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val spec = PBEKeySpec(passCode.toCharArray(), salt, ITERATION_COUNT, KEY_LENGTH)
        val keyBytes = factory.generateSecret(spec).encoded
        return SecretKeySpec(keyBytes, "AES")
    }

    fun encrypt(plainText: String, passCode: String): String {

        val salt = ByteArray(16)
        SecureRandom().nextBytes(salt)
        val key = deriveKeyFromPasscode(passCode = passCode, salt = salt)
        val cipher = Cipher.getInstance(AES_MODE)
        cipher.init(Cipher.ENCRYPT_MODE, key)
        val iv = cipher.iv

        val encryptedData = cipher.doFinal(plainText.toByteArray())

        val combined = salt + iv + encryptedData
        return Base64.encodeToString(combined, Base64.DEFAULT)
    }

    fun decrypt(encryptedText: String, passCode: String): String {
        val decoded = Base64.decode(encryptedText, Base64.DEFAULT)
        val salt = decoded.copyOfRange(0, 16)
        val iv = decoded.copyOfRange(16, 28)
        val data = decoded.copyOfRange(28, decoded.size)

        val key = deriveKeyFromPasscode(passCode = passCode, salt = salt)
        val cipher = Cipher.getInstance(AES_MODE)
        cipher.init(Cipher.DECRYPT_MODE, key, GCMParameterSpec(128, iv))
        return String(cipher.doFinal(data))
    }

    fun hashPasscode(passCode: String, salt: ByteArray): ByteArray {
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val spec = PBEKeySpec(passCode.toCharArray(), salt, ITERATION_COUNT, KEY_LENGTH)
        return factory.generateSecret(spec).encoded
    }

    fun isPasscodeCorrect(
        enteredPasscode: String, storedSalt: ByteArray, storedHash: ByteArray
    ): Boolean {
        val newHash = hashPasscode(enteredPasscode, storedSalt)
        return newHash.contentEquals(storedHash)
    }
}
