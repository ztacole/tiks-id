package com.zetta.tiksid.ui.screen.auth.signin

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.zetta.tiksid.ui.screen.auth.AuthViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun SignIn(
    onNavigateToHome: () -> Unit,
    onNavigateToSignUp: () -> Unit,
) {
    val viewModel: AuthViewModel = koinViewModel()

    SignInScreen(
        signInUiState = viewModel.signInUiState,
        onEmailChange = viewModel::updateSignInEmail,
        onPasswordChange = viewModel::updateSignInPassword,
        onSignInClick = viewModel::signIn,
        onClearAuthError = viewModel::clearValidationSignInState,
        onNavigateToHome = onNavigateToHome,
        onNavigateToSignUp = onNavigateToSignUp,
        modifier = Modifier
    )
}