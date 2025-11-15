package com.zetta.tiksid.ui.screen.auth.signin

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
