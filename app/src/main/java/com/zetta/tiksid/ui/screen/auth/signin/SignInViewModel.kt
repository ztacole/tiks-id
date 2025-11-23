package com.zetta.tiksid.ui.screen.auth.signin

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zetta.tiksid.R
import com.zetta.tiksid.data.repository.AuthRepository
import com.zetta.tiksid.utils.ResourceProvider
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SignInViewModel(
    private val resourceProvider: ResourceProvider,
    private val repository: AuthRepository
): ViewModel() {
    var signInUiState by mutableStateOf(SignInUiState())
        private set

    fun signIn() {
        viewModelScope.launch {
            clearValidationSignInState()
            if (!validateSignInInput()) return@launch
            signInUiState = signInUiState.copy(isAuthenticating = true)

            repository.login(signInUiState.email, signInUiState.password)
                .onSuccess {
                    signInUiState = signInUiState.copy(
                        authenticationSucceed = true,
                        isAuthenticating = false
                    )
                }
                .onFailure {
                    signInUiState = signInUiState.copy(
                        authenticationErrorMessage = it.message,
                        isAuthenticating = false,
                        password = ""
                    )
                }
        }
    }

    fun clearValidationSignInState() {
        signInUiState = signInUiState.copy(
            emailErrorMessage = null,
            passwordErrorMessage = null,
            isEmailError = false,
            isPasswordError = false,
            authenticationErrorMessage = null
        )
    }

    private fun validateSignInInput(): Boolean {
        // Email Validation
        if (signInUiState.email.isEmpty()) {
            signInUiState =
                signInUiState.copy(
                    emailErrorMessage = resourceProvider.getString(R.string.signin_input_required),
                    isEmailError = true
                )
        }
        if (signInUiState.email.isNotEmpty() && !Patterns.EMAIL_ADDRESS.matcher(signInUiState.email).matches()) {
            signInUiState =
                signInUiState.copy(
                    emailErrorMessage = resourceProvider.getString(R.string.signin_input_invalid_email),
                    isEmailError = true
                )
        }

        // Password Validation
        if (signInUiState.password.isEmpty()) {
            signInUiState =
                signInUiState.copy(
                    passwordErrorMessage = resourceProvider.getString(R.string.signin_input_required),
                    isPasswordError = true
                )
        }

        return !(signInUiState.isEmailError || signInUiState.isPasswordError)
    }

    fun updateSignInEmail(input: String) {
        signInUiState = signInUiState.copy(
            email = input,
            emailErrorMessage = null,
            isEmailError = false
        )
    }

    fun updateSignInPassword(input: String) {
        signInUiState = signInUiState.copy(
            password = input,
            passwordErrorMessage = null,
            isPasswordError = false
        )
    }
}