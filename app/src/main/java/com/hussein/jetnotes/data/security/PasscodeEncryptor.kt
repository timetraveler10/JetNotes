package com.hussein.jetnotes.data.security

import android.util.Base64
import java.security.SecureRandom
import javax.crypto.AEADBadTagException
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

object PasscodeEncryptor {

    // --- Security Parameters ---
    private const val ALGORITHM = "AES"
    private const val AES_MODE = "AES/GCM/NoPadding"
    private const val KEY_DERIVATION_ALGORITHM = "PBKDF2WithHmacSHA256"

    // Increased iteration count based on modern recommendations (OWASP 2023).
    private const val ITERATION_COUNT = 600_000
    private const val KEY_LENGTH_BITS = 256
    private const val SALT_LENGTH_BYTES = 16
    private const val IV_LENGTH_BYTES = 12 // Recommended length for GCM mode for best performance and security.
    private const val TAG_LENGTH_BITS = 128

    /**
     * Derives a secret key from a user-provided passcode and a salt using PBKDF2.
     *
     * @param passcode The user's passcode.
     * @param salt A random salt.
     * @return A SecretKeySpec suitable for AES encryption.
     */
    private fun deriveKeyFromPasscode(passcode: CharArray, salt: ByteArray): SecretKeySpec {
        val factory = SecretKeyFactory.getInstance(KEY_DERIVATION_ALGORITHM)
        val spec = PBEKeySpec(passcode, salt, ITERATION_COUNT, KEY_LENGTH_BITS)
        val keyBytes = factory.generateSecret(spec).encoded
        return SecretKeySpec(keyBytes, ALGORITHM)
    }

    /**
     * Encrypts a plaintext string using a passcode.
     *
     * This method generates a new random salt and IV for each encryption operation.
     * The final output is a Base64 encoded string containing [salt | IV | ciphertext].
     *
     * @param plainText The string to encrypt.
     * @param passcode The passcode to use for encryption.
     * @return A Base64 encoded string representing the encrypted data.
     */
    fun encrypt(plainText: String, passcode: String): String {
        // 1. Generate a random salt for key derivation.
        val salt = ByteArray(SALT_LENGTH_BYTES).also { SecureRandom().nextBytes(it) }

        // 2. Derive the encryption key from the passcode and salt.
        val key = deriveKeyFromPasscode(passcode.toCharArray(), salt)

        // 3. Generate a random IV for GCM mode.
        val iv = ByteArray(IV_LENGTH_BYTES).also { SecureRandom().nextBytes(it) }

        // 4. Encrypt the data.
        val cipher = Cipher.getInstance(AES_MODE)
        val gcmParameterSpec = GCMParameterSpec(TAG_LENGTH_BITS, iv)
        cipher.init(Cipher.ENCRYPT_MODE, key, gcmParameterSpec)

        val encryptedData = cipher.doFinal(plainText.toByteArray(Charsets.UTF_8))

        // 5. Combine salt, IV, and ciphertext into a single array for storage.
        val combined = salt + iv + encryptedData

        // 6. Encode the combined byte array to a Base64 string.
        return Base64.encodeToString(combined, Base64.NO_WRAP)
    }

    /**
     * Decrypts a Base64 encoded string using a passcode.
     *
     * @param encryptedText The Base64 encoded string [salt | IV | ciphertext].
     * @param passcode The passcode used for the original encryption.
     * @return The original plaintext string.
     * @throws IllegalArgumentException if the encrypted data is malformed or too short.
     * @throws AEADBadTagException if the passcode is incorrect or the data is corrupt (tag mismatch).
     * @throws IllegalStateException for other decryption failures.
     */
    fun decrypt(encryptedText: String, passcode: String): String {
        // 1. Decode the Base64 string into its raw byte components.
        val decoded = Base64.decode(encryptedText, Base64.DEFAULT)

        // 2. Validate the decoded data length.
        val minExpectedLength = SALT_LENGTH_BYTES + IV_LENGTH_BYTES + 1 // Salt + IV + at least 1 byte of data.
        if (decoded.size < minExpectedLength) {
            throw IllegalArgumentException("Invalid encrypted data: too short.")
        }

        // 3. Extract the salt, IV, and ciphertext from the decoded data.
        val salt = decoded.copyOfRange(0, SALT_LENGTH_BYTES)
        val iv = decoded.copyOfRange(SALT_LENGTH_BYTES, SALT_LENGTH_BYTES + IV_LENGTH_BYTES)
        val data = decoded.copyOfRange(SALT_LENGTH_BYTES + IV_LENGTH_BYTES, decoded.size)

        // 4. Re-derive the key using the extracted salt.
        val key = deriveKeyFromPasscode(passcode.toCharArray(), salt)

        // 5. Initialize the cipher for decryption.
        val cipher = Cipher.getInstance(AES_MODE)
        val gcmParameterSpec = GCMParameterSpec(TAG_LENGTH_BITS, iv)
        cipher.init(Cipher.DECRYPT_MODE, key, gcmParameterSpec)

        try {
            // 6. Decrypt the data and return as a string.
            val plain = cipher.doFinal(data)
            return String(plain, Charsets.UTF_8)
        } catch (e: AEADBadTagException) {
            // This is the most common error for a wrong passcode.
            throw AEADBadTagException("Decryption failed: incorrect passcode or corrupt data.")
        } catch (e: Exception) {
            // Catch other potential crypto exceptions.
            throw IllegalStateException("An unexpected error occurred during decryption.", e)
        }
    }

    /**
     * Generates a hash from a passcode and salt. This can be used to securely store a
     * representation of the passcode without storing the passcode itself.
     *
     * @param passcode The passcode to hash.
     * @param salt A random salt.
     * @return The hashed passcode as a byte array.
     */
    fun hashPasscode(passcode: String, salt: ByteArray): ByteArray {
        val spec = PBEKeySpec(passcode.toCharArray(), salt, ITERATION_COUNT, KEY_LENGTH_BITS)
        val factory = SecretKeyFactory.getInstance(KEY_DERIVATION_ALGORITHM)
        return factory.generateSecret(spec).encoded
    }

    /**
     * Verifies if an entered passcode matches a stored hash.
     *
     * @param enteredPasscode The passcode entered by the user.
     * @param storedSalt The salt that was used to create the stored hash.
     * @param storedHash The hash to compare against.
     * @return True if the passcode is correct, false otherwise.
     */
    fun isPasscodeCorrect(
        enteredPasscode: String,
        storedSalt: ByteArray,
        storedHash: ByteArray
    ): Boolean {
        val newHash = hashPasscode(enteredPasscode, storedSalt)
        // Use a constant-time comparison to prevent timing attacks.
        return newHash.contentEquals(storedHash)
    }
}
