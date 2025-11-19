package com.zetta.tiksid.ui.components.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zetta.tiksid.R
import com.zetta.tiksid.ui.theme.AppTheme

@Composable
fun TicketDisplay(
    title: String,
    genre: String,
    duration: Int,
    theater: String,
    schedule: String,
    seat: String,
    modifier: Modifier = Modifier
) {
    val surfaceColor = MaterialTheme.colorScheme.surface

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        shape = MaterialTheme.shapes.medium
    ) {
        Image(
            painter = painterResource(R.drawable.barcode),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            contentScale = ContentScale.FillBounds
        )
        Text(
            text = stringResource(R.string.ticket_detail_text_scan_barcode),
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray),
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .align(Alignment.CenterHorizontally)
        )
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
        ) {
            val canvasWidth = size.width
            val canvasHeight = size.height

            drawArc(
                color = surfaceColor,
                startAngle = -90f,
                sweepAngle = 180f,
                useCenter = true,
                topLeft = Offset(-60f, 0f),
                size = Size(width = 120f, height = canvasHeight),
            )
            drawArc(
                color = surfaceColor,
                startAngle = -90f,
                sweepAngle = -180f,
                useCenter = true,
                topLeft = Offset( canvasWidth - 60, 0f),
                size = Size(width = 120f, height = canvasHeight),
            )
            drawLine(
                color = Color.Gray,
                start = Offset(150f, canvasHeight / 2),
                end = Offset(canvasWidth - 150, canvasHeight / 2),
                strokeWidth = 6f,
                cap = StrokeCap.Round,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(30f, 24f))
            )
        }
        Spacer(Modifier.height(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .padding(vertical = 8.dp, horizontal = 24.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = pluralStringResource(R.plurals.text_movie_overview, duration, genre, duration),
            style = MaterialTheme.typography.bodyLarge.copy(color = Color.LightGray),
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(36.dp))
        Text(
            text = theater,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 8.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = schedule,
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray),
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Text(
            text = seat,
            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(40.dp),
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Preview
@Composable
private fun Prev() {
    AppTheme {
        Surface {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(24.dp),
                modifier = Modifier.padding(24.dp)
            ) {
                items(2) {
                    TicketDisplay(
                        title = "Deadpool & Wolverine",
                        genre = "Action",
                        duration = 128,
                        theater = "Diana TheaterSchedule",
                        schedule = "28 Oct 2024 08:00",
                        seat = "A${it + 1}"
                    )
                }
            }
        }
    }
}