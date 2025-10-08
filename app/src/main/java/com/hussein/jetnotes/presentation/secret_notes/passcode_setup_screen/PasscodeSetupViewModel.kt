package com.hussein.jetnotes.presentation.passcode_setup_screen

import android.util.Base64
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.hussein.jetnotes.JetNotesApp
import com.hussein.jetnotes.data.security.AppPreferences
import com.hussein.jetnotes.data.security.PasscodeEncryptor
import com.hussein.jetnotes.presentation.main_screen.MainViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.security.SecureRandom

class PasscodeSetupViewModel(val appPreferences: AppPreferences): ViewModel() {
    val state = MutableStateFlow(PasscodeSetupState())

    fun onAction(action: PasscodeSetupActions){
        when(action){

            PasscodeSetupActions.HandleSubmit -> handleSubmit()
            is PasscodeSetupActions.OnInputChange -> updateInput(action.value)
        }
    }

    private fun updateInput(value: String) {
        // Limit passcode length if desired, e.g., to 4 digits
        if (value.length <= 4) {
            state.update {
                it.copy(currentInput = value, errorMessage = null) // Clear error on new input
            }
        }
    }


    private fun handleSubmit() {
        if (state.value.currentInput.isEmpty()) return

        if (!state.value.isConfirmationStep) {
            // First step: Store the passcode and clear the input for the confirmation step.
            state.update {
                it.copy(
                    firstPasscodeAttempt = it.currentInput,
                    currentInput = "" // Clear input for the next step
                )
            }
        } else {
            // Second step: Compare the current input with the stored one.
            checkIfInputsAreEqual()
        }
    }

    private fun checkIfInputsAreEqual() {
        val firstAttempt = state.value.firstPasscodeAttempt
        val secondAttempt = state.value.currentInput

        if (firstAttempt == secondAttempt) {
            // Success! Save the new passcode.
            saveNewPasscode(firstAttempt!!)
        } else {
            // Failure: Passcodes don't match. Reset to the beginning.
            state.update {
                it.copy(
                    errorMessage = "Passcodes do not match. Please try again.",
                    currentInput = "",
                    firstPasscodeAttempt = null // Go back to step 1
                )
            }
        }
    }


    fun saveNewPasscode(passcode: String) {
        viewModelScope.launch {
            // 1. Generate a new, random salt
            val salt = ByteArray(16)
            SecureRandom().nextBytes(salt)

            // 2. Hash the passcode using the new salt
            val hash = PasscodeEncryptor.hashPasscode(passcode, salt)

            // 3. Encode both to Base64 strings for storage
            val saltString = Base64.encodeToString(salt, Base64.NO_WRAP)
            val hashString = Base64.encodeToString(hash, Base64.NO_WRAP)

            // 4. Save them to DataStore
            appPreferences.savePasscodeCredentials(saltString, hashString)

            state.update { it.copy(isPasscodeSaved = true) }
        }
    }

    companion object {
        val Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {

                val application = extras[APPLICATION_KEY] as JetNotesApp
                val container = application.appContainer

                return PasscodeSetupViewModel(container.appPreferences) as T
            }
        }
    }

}