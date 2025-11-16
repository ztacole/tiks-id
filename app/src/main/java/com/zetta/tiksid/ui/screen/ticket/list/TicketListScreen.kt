package com.zetta.tiksid.ui.screen.ticket.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zetta.tiksid.R
import com.zetta.tiksid.data.model.History
import com.zetta.tiksid.data.model.Movie
import com.zetta.tiksid.ui.components.screen.HistoryCard
import com.zetta.tiksid.ui.components.screen.ShimmerHistoryCard
import com.zetta.tiksid.ui.theme.AppTheme
import com.zetta.tiksid.utils.Constants

@Composable
fun TicketListScreen(
    uiState: TicketListUiState,
    onNavigateToTicketDetail: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        Spacer(Modifier.height(24.dp))
        Text(
            text = stringResource(R.string.ticket_list_text_your_ticket),
            style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp),
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        Spacer(Modifier.height(8.dp))
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            overscrollEffect = null,
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 24.dp + Constants.NAVIGATION_BAR_HEIGHT)
        ) {
            if (uiState.isLoading) {
                items(3) {
                    ShimmerHistoryCard()
                }
            } else {
                items(uiState.histories.size) { index ->
                    val history = uiState.histories[index]

                    HistoryCard(
                        history = history,
                        onCLick = onNavigateToTicketDetail
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun TicketListScreenPreview() {
    AppTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            TicketListScreen(
                uiState = TicketListUiState(
                    histories = List(12) { index ->
                        History(
                            id = index + 1,
                            movie = Movie(
                                id = index + 1,
                                title = "Movie ${index + 1}",
                                poster = "",
                                genre = "Action",
                                duration = 128
                            ),
                            schedule = "28 Oct 2024 08:00",
                            seats = List(5) { "A${it + 1}" },
                            totalPrice = 200000
                        )
                    }
                ),
                onNavigateToTicketDetail = {}
            )
        }
    }
}