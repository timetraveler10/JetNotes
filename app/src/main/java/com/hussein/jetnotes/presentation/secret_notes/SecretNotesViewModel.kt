package com.hussein.jetnotes.presentation.secret_notes 

import android.util.Base64
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.hussein.jetnotes.JetNotesApp
import com.hussein.jetnotes.data.preferences.SecurityPreferences
import com.hussein.jetnotes.data.repository.NoteRepository
import com.hussein.jetnotes.data.security.PasscodeEncryptor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.security.SecureRandom

class SecretNotesViewModel(
    val repository: NoteRepository,
    val securityPreferences: SecurityPreferences
) : ViewModel() {


    val state = MutableStateFlow(SecretNotesScreenState())


    fun onAction(action: SecretNoteActions) {
        when (action) {

            SecretNoteActions.HandlePasscodeSetupSubmit -> handleSubmit()
            is SecretNoteActions.OnPasscodeInputChange -> updateInput(action.value)
            SecretNoteActions.CheckIfPasscodeExists -> checkForSavedPasscode()
            is SecretNoteActions.HandlePasscodeEntrySubmit -> handlePasscodeEntrySubmit(action.rawPassword)
        }
    }

    private fun handlePasscodeEntrySubmit(enteredPasscode: String) {
        viewModelScope.launch {
            val savedSalt = securityPreferences.getPasscodeSalt.first()
            val savedPasswordHash = securityPreferences.getPasscodeHash.first()

            val decodedSalt = Base64.decode(savedSalt, Base64.DEFAULT)
            val decodedPassword = Base64.decode(savedPasswordHash, Base64.DEFAULT)

            val isPasswordCorrect = PasscodeEncryptor.isPasscodeCorrect(
                enteredPasscode = enteredPasscode,
                storedSalt = decodedSalt,
                storedHash = decodedPassword
            )

            if (isPasswordCorrect) {
                // here we should fetch all the secret notes
                state.update {
                    it.copy(
                        isAuthSuccessful = true,
                        currentInput = "",
                        passcodeErrorMessage = null,
                        firstPasscodeAttempt = null
                    )
                }
            } else {
                state.update {
                    it.copy(
                        passcodeErrorMessage = "Incorrect passcode",
                        currentInput = ""
                    )
                }
            }
        }
    }
