package com.zetta.tiksid.ui.screen.ticket.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.zetta.tiksid.ui.components.EmptyBox
import com.zetta.tiksid.ui.components.screen.TicketDisplay
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
        when {
            uiState.isLoading -> {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(MaterialTheme.shapes.medium)
                            .aspectRatio(2/3f)
                            .shimmerLoading()
                    )
                }
            }
            uiState.errorMessage != null -> {
                item {
                    EmptyBox(
                        modifier = Modifier.fillMaxSize(),
                        text = uiState.errorMessage,
                    )
                }
            }
            uiState.ticket == null -> {
                item { Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface)) }
            }
            else -> {
                items(uiState.ticket.seats.size) { index ->
                    val seat = uiState.ticket.seats[index]

                    TicketDisplay(
                        title = uiState.ticket.movieTitle,
                        genre = uiState.ticket.movieGenres[0],
                        duration = uiState.ticket.movieDuration,
                        theater = uiState.ticket.theaterName,
                        schedule = uiState.ticket.scheduleDate,
                        seat = seat,
                    )
                }
            }
        }
    }
}