package com.zetta.tiksid.ui.screen.auth.signin

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zetta.tiksid.R
import com.zetta.tiksid.ui.components.AppButton
import com.zetta.tiksid.ui.components.AppDialog
import com.zetta.tiksid.ui.components.AppTextField
import com.zetta.tiksid.ui.theme.AppTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SignInScreen(
    signInUiState: SignInUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSignInClick: () -> Unit,
    onClearAuthError: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToSignUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    val imeVisible = WindowInsets.isImeVisible
    val focusManager = LocalFocusManager.current

    var isPasswordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(
        key1 = signInUiState.authenticationSucceed
    ) {
        if (signInUiState.authenticationSucceed) onNavigateToHome()
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .imePadding(),
        overscrollEffect = null,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            Row(
                modifier = Modifier
                    .padding(top = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)
                )
                Text(
                    text = stringResource(R.string.app_name),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
        item {
            Spacer(Modifier.height(150.dp))
            Box(
                modifier = modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .padding(top = if (imeVisible) 0.dp else 56.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.signin_text_headline),
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = stringResource(R.string.signin_text_subheadline),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(Modifier.height(30.dp))
                    AppTextField(
                        value = signInUiState.email,
                        onValueChange = onEmailChange,
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = stringResource(R.string.signin_input_email_placeholder),
                        isError = signInUiState.isEmailError,
                        errorMessage = signInUiState.emailErrorMessage,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next,
                            capitalization = KeyboardCapitalization.None
                        ),
                        keyboardActions = KeyboardActions {
                            focusManager.moveFocus(FocusDirection.Down)
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Email,
                                contentDescription = null
                            )
                        }
                    )
                    Spacer(Modifier.height(20.dp))
                    AppTextField(
                        value = signInUiState.password,
                        onValueChange = onPasswordChange,
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = stringResource(R.string.signin_input_password_placeholder),
                        isError = signInUiState.isPasswordError,
                        errorMessage = signInUiState.passwordErrorMessage,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done,
                            capitalization = KeyboardCapitalization.None
                        ),
                        keyboardActions = KeyboardActions {
                            focusManager.clearFocus()
                            onSignInClick()
                        },
                        hideValue = !isPasswordVisible,
                        trailingIcon = {
                            IconButton(
                                onClick = { isPasswordVisible = !isPasswordVisible },
                            ) {
                                Icon(
                                    imageVector = if (isPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                    contentDescription = null
                                )
                            }
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Lock,
                                contentDescription = null
                            )
                        }
                    )
                    Spacer(Modifier.height(40.dp))

                    AppButton(
                        text = stringResource(R.string.signin_button_sign_in),
                        onClick = onSignInClick,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            Spacer(Modifier.height(180.dp))
        }
        item {
            Row(
                modifier = Modifier.navigationBarsPadding(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = stringResource(R.string.signin_text_dont_have_account),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = stringResource(R.string.signin_text_sign_up),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { onNavigateToSignUp() }
                )
            }
        }
    }

    // Loading Dialog
    AppDialog(
        show = signInUiState.isAuthenticating,
        onDismiss = { },
        content = { CircularProgressIndicator(modifier = Modifier.padding(vertical = 16.dp)) }
    )

    // Auth Failed Dialog
    AppDialog(
        show = signInUiState.authenticationErrorMessage != null,
        onDismiss = { },
        title = stringResource(R.string.signin_dialog_title_failed),
        content = {
            Text(
                text = signInUiState.authenticationErrorMessage!!,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        },
        confirmButton = {
            AppButton(
                text = stringResource(R.string.text_ok),
                onClick = onClearAuthError
            )
        }
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF0F0F16)
@Composable
private fun LoginScreenPreview() {
    AppTheme {
        SignInScreen(
            signInUiState = SignInUiState(),
            onEmailChange = {},
            onPasswordChange = {},
            onSignInClick = {},
            onNavigateToHome = {},
            onClearAuthError = {},
            onNavigateToSignUp = {}
        )
    }
}