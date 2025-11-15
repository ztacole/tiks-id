package com.zetta.tiksid.ui.screen.auth.signup

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
