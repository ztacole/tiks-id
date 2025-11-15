package com.zetta.tiksid.ui.screen.auth

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zetta.tiksid.R
import com.zetta.tiksid.utils.ResourceProvider
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AuthViewModel(private val resourceProvider: ResourceProvider): ViewModel() {
    var signInUiState by mutableStateOf(SignInUiState())
        private set

    var signUpUiState by mutableStateOf(SignUpUiState())
        private set

    fun signIn() {
        viewModelScope.launch {
            clearValidationSignInState()
            if (!validateSignInInput()) return@launch

            signInUiState = signInUiState.copy(isAuthenticating = true)
            delay(2000)
            signInUiState = signInUiState.copy(isAuthenticating = false, authenticationSucceed = true)
//            repository.signIn(signInUiState.email, signInUiState.password)
//                .onSuccess {
//                    signInUiState = signInUiState.copy(
//                        authenticationSucceed = true,
//                        isAuthenticating = false
//                    )
//                }
//                .onFailure {
//                    signInUiState = signInUiState.copy(
//                        authenticationErrorMessage = it.message,
//                        isAuthenticating = false,
//                        password = ""
//                    )
//                }
        }
    }

    fun signUp() {
        viewModelScope.launch {
            clearValidationSignUpState()
            if (!validateSignUpInput()) return@launch

            signUpUiState = signUpUiState.copy(isAuthenticating = true)
            delay(2000)
            signUpUiState = signUpUiState.copy(isAuthenticating = false, authenticationSucceed = true)
//            repository.signUp(signUpUiState.name, signUpUiState.email, signUpUiState.password, signUpUiState.confirmPassword)
//                .onSuccess {
//                    signInUiState = signUpUiState.copy(
//                        authenticationSucceed = true,
//                        isAuthenticating = false
//                    )
//                }
//                .onFailure {
//                    signInUiState = signUpUiState.copy(
//                        authenticationErrorMessage = it.message,
//                        isAuthenticating = false,
//                    )
//                }
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

    fun clearValidationSignUpState() {
        signUpUiState = signUpUiState.copy(
            nameErrorMessage = null,
            emailErrorMessage = null,
            passwordErrorMessage = null,
            confirmPasswordErrorMessage = null,
            isNameError = false,
            isEmailError = false,
            isPasswordError = false,
            isConfirmPasswordError = false,
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

    private fun validateSignUpInput(): Boolean {
        // Name Validation
        if (signUpUiState.name.isEmpty()) {
            signUpUiState =
                signUpUiState.copy(
                    nameErrorMessage = resourceProvider.getString(R.string.signup_input_required),
                    isNameError = true
                )
        }
        if (signUpUiState.name.isNotEmpty() && signUpUiState.name.length < 3 || signUpUiState.name.length > 50) {
            signUpUiState =
                signUpUiState.copy(
                    nameErrorMessage = resourceProvider.getString(R.string.signup_input_invalid_name_length),
                    isNameError = true
                )
        }

        // Email Validation
        if (signUpUiState.email.isEmpty()) {
            signUpUiState =
                signUpUiState.copy(
                    emailErrorMessage = resourceProvider.getString(R.string.signup_input_required),
                    isEmailError = true
                )
        }
        if (signUpUiState.email.isNotEmpty() && !Patterns.EMAIL_ADDRESS.matcher(signUpUiState.email).matches()) {
            signUpUiState =
                signUpUiState.copy(
                    emailErrorMessage = resourceProvider.getString(R.string.signup_input_invalid_email),
                    isEmailError = true
                )
        }

        // Password Validation
        if (signUpUiState.password.isEmpty()) {
            signUpUiState =
                signUpUiState.copy(
                    passwordErrorMessage = resourceProvider.getString(R.string.signup_input_required),
                    isPasswordError = true
                )
        }
        if (signUpUiState.password.isNotEmpty() && signUpUiState.password.length < 8) {
            signUpUiState =
                signUpUiState.copy(
                    passwordErrorMessage = resourceProvider.getString(R.string.signup_input_invalid_password_length),
                    isPasswordError = true
                )
        }

        // Confirm Password Validation
        if (signUpUiState.confirmPassword.isEmpty()) {
            signUpUiState =
                signUpUiState.copy(
                    confirmPasswordErrorMessage = resourceProvider.getString(R.string.signup_input_required),
                    isConfirmPasswordError = true
                )
        }
        if (signUpUiState.password.isNotEmpty() && signUpUiState.confirmPassword.isNotEmpty() && signUpUiState.password != signUpUiState.confirmPassword) {
            signUpUiState = signUpUiState.copy(
                confirmPasswordErrorMessage = resourceProvider.getString(R.string.signup_input_password_mismatch),
                isConfirmPasswordError = true
            )
        }

        return !(signUpUiState.isNameError || signUpUiState.isEmailError || signUpUiState.isPasswordError || signUpUiState.isConfirmPasswordError)
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

    fun updateSignUpName(input: String) {
        signUpUiState = signUpUiState.copy(
            name = input,
            nameErrorMessage = null,
            isNameError = false
        )
    }

    fun updateSignUpEmail(input: String) {
        signUpUiState = signUpUiState.copy(
            email = input,
            emailErrorMessage = null,
            isEmailError = false
        )
    }

    fun updateSignUpPassword(input: String) {
        signUpUiState = signUpUiState.copy(
            password = input,
            passwordErrorMessage = null,
            isPasswordError = false
        )
    }

    fun updateSignUpConfirmPassword(input: String) {
        signUpUiState = signUpUiState.copy(
            confirmPassword = input,
            confirmPasswordErrorMessage = null,
            isConfirmPasswordError = false
        )
    }

    data class SignInUiState(
        val email: String = "",
        val emailErrorMessage: String? = null,
        val isEmailError: Boolean = false,
        val password: String = "",
        val passwordErrorMessage: String? = null,
        val isPasswordError: Boolean = false,
        val isAuthenticating: Boolean = false,
        val authenticationSucceed: Boolean = false,
        val authenticationErrorMessage: String? = null
    )

    data class SignUpUiState(
        val name: String = "",
        val nameErrorMessage: String? = null,
        val isNameError: Boolean = false,
        val email: String = "",
        val emailErrorMessage: String? = null,
        val isEmailError: Boolean = false,
        val password: String = "",
        val passwordErrorMessage: String? = null,
        val isPasswordError: Boolean = false,
        val confirmPassword: String = "",
        val confirmPasswordErrorMessage: String? = null,
        val isConfirmPasswordError: Boolean = false,
        val isAuthenticating: Boolean = false,
        val authenticationSucceed: Boolean = false,
        val authenticationErrorMessage: String? = null
    )
}