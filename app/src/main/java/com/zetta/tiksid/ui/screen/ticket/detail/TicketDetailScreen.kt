package com.zetta.tiksid.ui.screen.ticket.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zetta.tiksid.data.model.Movie
import com.zetta.tiksid.data.model.TicketDetail
import com.zetta.tiksid.ui.components.BackButton
import com.zetta.tiksid.ui.components.screen.TicketDisplay
import com.zetta.tiksid.ui.theme.AppTheme
import com.zetta.tiksid.utils.shimmerLoading

@Composable
fun TicketDetailScreen(
    uiState: TicketDetailUiState,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        contentPadding = PaddingValues(top = 24.dp, bottom = 48.dp, start = 24.dp, end = 24.dp),
        overscrollEffect = null
    ) {
        if (uiState.isLoading || uiState.ticket == null) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.medium)
                        .aspectRatio(2/3f)
                        .shimmerLoading()
                )
            }
        } else {
            items(uiState.ticket.seats.size) { index ->
                val seat = uiState.ticket.seats[index]

                TicketDisplay(
                    title = uiState.ticket.movie.title,
                    genre = uiState.ticket.movie.genre[0],
                    duration = uiState.ticket.movie.duration,
                    theater = uiState.ticket.theater,
                    schedule = uiState.ticket.schedule,
                    seat = seat,
                )
            }
        }
    }
}

@Preview
@Composable
private fun Prev() {
    AppTheme {
        Surface {
            TicketDetailScreen(
                uiState = TicketDetailUiState(
                    ticket = TicketDetail(
                        id = 1,
                        movie = Movie(
                            id = 1,
                            title = "Deadpool & Wolverine",
                            genre = listOf("Action"),
                            duration = 128,
                            poster = ""
                        ),
                        theater = "Diana TheaterSchedule",
                        schedule = "28 Oct 2024 08:00",
                        seats = List(5) { "A${it + 1}" }
                    )
                )
            )
        }
    }
}