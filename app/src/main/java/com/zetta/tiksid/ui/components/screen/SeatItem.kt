package com.zetta.tiksid.ui.components.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zetta.tiksid.ui.screen.movie.detail.Seat
import com.zetta.tiksid.ui.screen.movie.detail.SeatStatus
import com.zetta.tiksid.ui.theme.AppTheme

@Composable
fun SeatItem(
    seat: Seat,
    status: SeatStatus,
    onSeatSelected: (String) -> Unit
) {
    val textColor = when(status) {
        SeatStatus.UNAVAILABLE -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.35f)
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    val backgroundColor = when(status){
        SeatStatus.AVAILABLE -> MaterialTheme.colorScheme.surfaceVariant
        SeatStatus.SELECTED -> MaterialTheme.colorScheme.primary
        SeatStatus.UNAVAILABLE -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
    }

    Box(
        modifier = Modifier
            .padding(2.dp)
            .size(40.dp)
            .clip(MaterialTheme.shapes.extraSmall)
            .background(backgroundColor,)
            .clickable(enabled = status != SeatStatus.UNAVAILABLE) { onSeatSelected(seat.id) },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = seat.displayLabel,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            maxLines = 1,
            color = textColor
        )
    }

}

@Preview
@Composable
private fun Prev() {
    AppTheme {
        Surface {
            SeatItem(
                seat = Seat(
                    id = "A1",
                    row = "A",
                    column = 1,
                    sectionId = "A",
                    displayLabel = "A1"
                ),
                status = SeatStatus.AVAILABLE,
                onSeatSelected = {}
            )
        }
    }
}