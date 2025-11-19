package com.zetta.tiksid.ui.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.semantics.error
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zetta.tiksid.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.bodySmall,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    placeholder: String? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    hideValue: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = if (!singleLine) 2 else 1,
    interactionSource: MutableInteractionSource? = null,
    shape: Shape = MaterialTheme.shapes.small,
    errorMessage: String? = null,
    outlineMode: Boolean = false
) {
    val plainColors = TextFieldDefaults.colors(
        errorIndicatorColor = Color.Transparent,
        errorContainerColor = MaterialTheme.colorScheme.errorContainer,
        errorTextColor = MaterialTheme.colorScheme.onErrorContainer,
        errorCursorColor = MaterialTheme.colorScheme.onBackground,
        errorPlaceholderColor = MaterialTheme.colorScheme.onErrorContainer,
        focusedIndicatorColor = Color.Transparent,
        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
        focusedTextColor = MaterialTheme.colorScheme.onBackground,
        unfocusedIndicatorColor = Color.Transparent,
        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
        unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
        disabledIndicatorColor = Color.Transparent,
        disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
        disabledTextColor = MaterialTheme.colorScheme.outline,
        focusedLeadingIconColor = MaterialTheme.colorScheme.onSurface,
        focusedTrailingIconColor = MaterialTheme.colorScheme.onSurface,
        unfocusedLeadingIconColor = MaterialTheme.colorScheme.onSurface.copy(0.5f),
        unfocusedTrailingIconColor = MaterialTheme.colorScheme.onSurface.copy(0.5f),
        errorLeadingIconColor = MaterialTheme.colorScheme.onErrorContainer,
        errorTrailingIconColor = MaterialTheme.colorScheme.onErrorContainer,
        cursorColor = MaterialTheme.colorScheme.onBackground,
    )
    val outlineColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = MaterialTheme.colorScheme.outline,
        unfocusedBorderColor = MaterialTheme.colorScheme.surfaceVariant,
        disabledBorderColor = MaterialTheme.colorScheme.surfaceVariant,
        disabledTextColor = MaterialTheme.colorScheme.outline,
        errorBorderColor = MaterialTheme.colorScheme.errorContainer,
        errorContainerColor = MaterialTheme.colorScheme.errorContainer,
        errorTextColor = MaterialTheme.colorScheme.onErrorContainer,
        errorCursorColor = MaterialTheme.colorScheme.onBackground,
        errorPlaceholderColor = MaterialTheme.colorScheme.onErrorContainer,
        focusedContainerColor = Color.Transparent,
        focusedTextColor = MaterialTheme.colorScheme.onBackground,
        unfocusedContainerColor = Color.Transparent,
        unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
        focusedLeadingIconColor = MaterialTheme.colorScheme.onSurface,
        focusedTrailingIconColor = MaterialTheme.colorScheme.onSurface,
        unfocusedLeadingIconColor = MaterialTheme.colorScheme.onSurface.copy(0.5f),
        unfocusedTrailingIconColor = MaterialTheme.colorScheme.onSurface.copy(0.5f),
        errorLeadingIconColor = MaterialTheme.colorScheme.onErrorContainer,
        errorTrailingIconColor = MaterialTheme.colorScheme.onErrorContainer,
        cursorColor = MaterialTheme.colorScheme.onBackground,
    )
    val interactionSource = interactionSource ?: remember { MutableInteractionSource() }
    val textColor =
        textStyle.color.takeOrElse {
            val focused = interactionSource.collectIsFocusedAsState().value
            plainColors.textColor(enabled, isError, focused)
        }
    val mergedTextStyle = textStyle.merge(TextStyle(color = textColor))
    val visualTransformation =
        if (hideValue) PasswordVisualTransformation() else VisualTransformation.None

    CompositionLocalProvider(LocalTextSelectionColors provides plainColors.textSelectionColors) {
        Column(
            modifier = modifier
        ) {
            BasicTextField(
                value = value,
                modifier =
                    Modifier
                        .defaultErrorSemantics(isError, errorMessage ?: "This field is required")
                        .defaultMinSize(minWidth = TextFieldDefaults.MinWidth)
                        .heightIn(min = 48.dp)
                        .fillMaxWidth(),
                onValueChange = onValueChange,
                enabled = enabled,
                readOnly = readOnly,
                textStyle = mergedTextStyle,
                cursorBrush = SolidColor(
                    if (outlineMode) outlineColors.cursorColor(isError)
                    else plainColors.cursorColor(isError)
                ),
                visualTransformation = visualTransformation,
                keyboardOptions = keyboardOptions,
                keyboardActions = keyboardActions,
                interactionSource = interactionSource,
                singleLine = singleLine,
                maxLines = maxLines,
                minLines = minLines,
                decorationBox =
                    @Composable { innerTextField ->
                        if (outlineMode) {
                            OutlinedTextFieldDefaults.DecorationBox(
                                value = value,
                                visualTransformation = visualTransformation,
                                innerTextField = innerTextField,
                                placeholder = placeholder?.let {
                                    {
                                        Text(
                                            text = it,
                                            style = textStyle,
                                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
                                        )
                                    }
                                },
                                leadingIcon = leadingIcon,
                                trailingIcon = trailingIcon,
                                prefix = prefix,
                                suffix = suffix,
                                singleLine = singleLine,
                                enabled = enabled,
                                isError = isError,
                                interactionSource = interactionSource,
                                colors = outlineColors,
                                container = {
                                    OutlinedTextFieldDefaults.Container(
                                        enabled = enabled,
                                        isError = isError,
                                        interactionSource = interactionSource,
                                        colors = outlineColors,
                                        shape = shape,
                                    )
                                },
                                contentPadding = TextFieldDefaults.contentPaddingWithoutLabel(
                                    top = 0.dp,
                                    bottom = 0.dp
                                )
                            )
                        } else {
                            TextFieldDefaults.DecorationBox(
                                value = value,
                                visualTransformation = visualTransformation,
                                innerTextField = innerTextField,
                                placeholder = placeholder?.let {
                                    {
                                        Text(
                                            text = it,
                                            style = textStyle,
                                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
                                        )
                                    }
                                },
                                leadingIcon = leadingIcon,
                                trailingIcon = trailingIcon,
                                prefix = prefix,
                                suffix = suffix,
                                shape = shape,
                                singleLine = singleLine,
                                enabled = enabled,
                                isError = isError,
                                interactionSource = interactionSource,
                                colors = plainColors,
                                contentPadding = TextFieldDefaults.contentPaddingWithoutLabel(
                                    top = 0.dp,
                                    bottom = 0.dp
                                )
                            )
                        }
                    }
            )
            errorMessage?.let {
                if (isError) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                    )
                }
            }
        }
    }
}

private fun Modifier.defaultErrorSemantics(
    isError: Boolean,
    defaultErrorMessage: String
): Modifier = if (isError) semantics { error(defaultErrorMessage) } else this

private fun TextFieldColors.textColor(
    enabled: Boolean,
    isError: Boolean,
    focused: Boolean
): Color =
    when {
        !enabled -> disabledTextColor
        isError -> errorTextColor
        focused -> focusedTextColor
        else -> unfocusedTextColor
    }

private fun TextFieldColors.cursorColor(isError: Boolean): Color =
    if (isError) errorCursorColor else cursorColor

@Preview(showBackground = true, backgroundColor = 0xFFF5FBF5)
@Composable
private fun LightPreview() {
    var value by remember { mutableStateOf("") }
    AppTheme {
        Column {
            AppTextField(
                value = value,
                onValueChange = { value = it },
                errorMessage = "This is error message",
                placeholder = "This is placeholder",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
            )
            AppTextField(
                value = value,
                onValueChange = { value = it },
                errorMessage = "This is error message",
                placeholder = "This is placeholder",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
                outlineMode = true
            )
        }
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES, backgroundColor = 0xFF0F1511)
@Composable
private fun DarkPreview() {
    var value by remember { mutableStateOf("") }
    AppTheme {
        Column {
            AppTextField(
                value = value,
                onValueChange = { value = it },
                errorMessage = "This is error message",
                placeholder = "This is placeholder",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
            )
            AppTextField(
                value = value,
                onValueChange = { value = it },
                errorMessage = "This is error message",
                placeholder = "This is placeholder",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
                outlineMode = true
            )
        }
    }
}