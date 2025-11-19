package com.zetta.tiksid.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.zetta.tiksid.ui.theme.AppTheme

@Composable
fun AppDialog(
    show: Boolean,
    onDismiss: () -> Unit,
    dismissOnBackPress: Boolean = true,
    dismissOnOutsideClick: Boolean = true,
    title: String? = null,
    confirmButton: @Composable (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    if (show) {
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(
                windowTitle = title ?: "",
                dismissOnBackPress = dismissOnBackPress,
                dismissOnClickOutside = dismissOnOutsideClick
            )
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.padding(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.Companion.CenterHorizontally
                ) {
                    title?.let {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.Companion.padding(bottom = 12.dp),
                            textAlign = TextAlign.Companion.Center,
                        )
                    }
                    content()
                    Spacer(Modifier.height(32.dp))
                    confirmButton?.invoke()
                }
            }
        }
    }
}

@Preview
@Composable
private fun AppDialogPreview() {
    AppTheme {
        AppDialog(
            show = true,
            onDismiss = {},
            title = "Title",
            content = {
                Text("Suka suka saya dfdfdfdfdfdfdfdfdfdddfdfdfdfdfdfd fdfdf")
            },
            confirmButton = {
                AppButton(
                    text = "Button",
                    onClick = {}
                )
            }
        )
    }
}