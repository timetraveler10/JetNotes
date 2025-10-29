package com.hussein.jetnotes.presentation.secret_notes

import android.util.Base64
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.hussein.jetnotes.JetNotesApp
import com.hussein.jetnotes.data.models.SecretNote
import com.hussein.jetnotes.data.preferences.SecurityPreferences
import com.hussein.jetnotes.data.repository.SecretNotesRepository
import com.hussein.jetnotes.data.security.PasscodeEncryptor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.security.SecureRandom

class SecretNotesViewModel(
    val repository: SecretNotesRepository,
    val securityPreferences: SecurityPreferences
) : ViewModel() {

    private var authenticatedPasscode: String? = null
    val state = MutableStateFlow(SecretNotesScreenState())


    private var currentPage = 0
    private var isFetching = false
    private var allNotesLoaded = false
    private val pageSize = 10

    fun onAction(action: SecretNoteActions) {
        when (action) {

            SecretNoteActions.HandlePasscodeSetupSubmit -> onHandlePasscodeSetupSubmit()
            is SecretNoteActions.OnPasscodeInputChange -> onUpdatePasscodeInput(action.value)
            SecretNoteActions.CheckIfPasscodeExists -> checkForSavedPasscode()
            is SecretNoteActions.HandlePasscodeEntrySubmit -> onHandlePasscodeEntrySubmit(action.rawPassword)
            SecretNoteActions.LoadNextPage -> onLoadNextPage()
        }
    }

    private fun prePopulate() {
        val title = "emv"
        val content = "title"
        repeat(10) {
            insertNote(
                SecretNote(
                    id = 0,
                    title = title + "$it",
                    content = content,
                    date = System.currentTimeMillis()
                )
            )
        }
    }

    fun insertNote(note: SecretNote) {
        viewModelScope.launch {
            val eTitle = PasscodeEncryptor.encrypt(note.title, authenticatedPasscode!!)
            val eContent = PasscodeEncryptor.encrypt(note.content, authenticatedPasscode!!)
            repository.insertNote(
                SecretNote(
                    id = 0,
                    title = eTitle,
                    content = eContent,
                    date = System.currentTimeMillis()
                )
            )
        }
    }

    private fun onLoadNextPage() {
        val passcode = authenticatedPasscode ?: return
        if (isFetching || allNotesLoaded) return

        viewModelScope.launch {
            isFetching = true
            state.update { it.copy(isLoading = true) }

            val offset = currentPage * pageSize
            val newEncryptedNotes =
                repository.getPaginatedSecretNotes(limit = pageSize, offset = offset)
            Log.d("SecretNotesViewModel", "Fetched ${newEncryptedNotes.size} encrypted notes.")

            if (newEncryptedNotes.isNotEmpty()) {
                val decryptedNotes = withContext(Dispatchers.Default) {
                    newEncryptedNotes.map { encryptedNote ->
                        async {
                            try {
                                val decryptedTitleFuture =
                                    async { PasscodeEncryptor.decrypt(encryptedNote.title, passcode) }
                                val decryptedContentFuture =
                                   async { PasscodeEncryptor.decrypt(encryptedNote.content, passcode) }
                                encryptedNote.copy(
                                    title = decryptedTitleFuture.await(),
                                    content = decryptedContentFuture.await())
                            } catch (e: Exception) {
                                Log.e("SecretNotesViewModel", "Failed to decrypt note id=${encryptedNote.id}")
                                null
                            }
                        }
                    }.awaitAll().filterNotNull()
                }

                val newList = state.value.notes + decryptedNotes
                Log.d("SecretNotesViewModel", "Decryption complete. New list size: ${newList.size}")
                state.update { old -> old.copy(notes = newList, isLoading = false) }
                currentPage++
            } else {
                allNotesLoaded = true
                state.update { it.copy(isLoading = false) }
            }

            isFetching = false
        }
    }


    private fun onHandlePasscodeEntrySubmit(enteredPasscode: String) {
        viewModelScope.launch {
            val savedSalt = securityPreferences.getPasscodeSalt.first()
            val savedPasswordHash = securityPreferences.getPasscodeHash.first()
            if (savedSalt == null || savedPasswordHash == null) {
                state.update {
                    it.copy(
                        passcodeErrorMessage = "Passcode not set up correctly.",
                        currentInput = ""
                    )
                }
                return@launch
            }
            val decodedSalt = async { Base64.decode(savedSalt, Base64.NO_WRAP) }
            val decodedPassword = async { Base64.decode(savedPasswordHash, Base64.NO_WRAP) }

            val isPasswordCorrect = PasscodeEncryptor.isPasscodeCorrect(
                enteredPasscode = enteredPasscode,
                storedSalt = decodedSalt.await(),
                storedHash = decodedPassword.await()
            )

            if (isPasswordCorrect) {
                // here we should fetch all the secret notes
                authenticatedPasscode = enteredPasscode
//                prePopulate()
                state.update {
                    it.copy(
                        isAuthSuccessful = true,
                        currentInput = "",
                        passcodeErrorMessage = null,
                        firstPasscodeAttempt = null
                    )
                }
                onLoadNextPage()
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

    private fun onUpdatePasscodeInput(value: String) {
        if (value.length <= 4) {
            state.update {
                it.copy(
                    currentInput = value,
                    passcodeErrorMessage = null
                )
            }
        }
    }

    private fun onHandlePasscodeSetupSubmit() {
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
            onComparingPasscodeSetupInputs()
        }
    }

    private fun onComparingPasscodeSetupInputs() {
        val firstAttempt = state.value.firstPasscodeAttempt
        val secondAttempt = state.value.currentInput

        if (firstAttempt == secondAttempt) {
            // Success! Save the new passcode.
            onSaveNewPasscode(firstAttempt)
            state.update { it.copy(isPasscodeSet = true) }
        } else {
            // Failure: Passcodes don't match. Reset to the beginning.
            state.update {
                it.copy(
                    passcodeErrorMessage = "Passcodes do not match. Please try again.",
                    currentInput = "",
                    firstPasscodeAttempt = null // Go back to step 1
                )
            }
        }
    }

    private fun onSaveNewPasscode(passcode: String) {
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
            securityPreferences.savePasscodeCredentials(saltString, hashString)

            state.update { it.copy(isPasscodeSet = true) }
        }
    }

    override fun onCleared() {
        authenticatedPasscode = null
        super.onCleared()
    }

    private fun checkForSavedPasscode() {
        viewModelScope.launch {
            val savedSalt = securityPreferences.getPasscodeSalt.first()
            val savedHash = securityPreferences.getPasscodeHash.first()
            state.update { old ->
                old.copy(
                    isPasscodeSet = savedSalt != null && savedHash != null,
                    isLoading = false
                )
            }
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

                return SecretNotesViewModel(
                    container.secretNotesRepository,
                    container.securityPreferences
                ) as T
            }
        }
    }
}