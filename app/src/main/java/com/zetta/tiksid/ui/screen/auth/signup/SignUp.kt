package com.zetta.tiksid.ui.screen.auth.signup

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.zetta.tiksid.ui.screen.auth.AuthViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun SignUp(
    onNavigateToSignIn: () -> Unit,
) {
    val viewModel: AuthViewModel = koinViewModel()

    SignUpScreen(
        signUpUiState = viewModel.signUpUiState,
        onNameChange = viewModel::updateSignUpName,
        onEmailChange = viewModel::updateSignUpEmail,
        onPasswordChange = viewModel::updateSignUpPassword,
        onConfirmPasswordChange = viewModel::updateSignUpConfirmPassword,
        onSignUpClick = viewModel::signUp,
        onClearAuthError = viewModel::clearValidationSignUpState,
        onNavigateToSignIn = onNavigateToSignIn,
        modifier = Modifier
    )
}