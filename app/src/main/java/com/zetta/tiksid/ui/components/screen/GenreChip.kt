package com.zetta.tiksid.ui.components.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zetta.tiksid.ui.theme.AppTheme

@Composable
fun GenreChip(genre: String) {
    Text(
        text = genre,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.bodySmall,
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.tertiary,
                shape = MaterialTheme.shapes.extraSmall,
            )
            .padding(horizontal = 12.dp, vertical = 4.dp)
    )
}

@Preview
@Composable
private fun Prev() {
    AppTheme {
        Surface {
            GenreChip("Action")
        }
    }
}