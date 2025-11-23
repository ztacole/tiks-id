package com.zetta.tiksid.ui.screen.ticket.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zetta.tiksid.R
import com.zetta.tiksid.ui.components.EmptyBox
import com.zetta.tiksid.ui.components.screen.HistoryCard
import com.zetta.tiksid.ui.components.screen.ShimmerHistoryCard
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
        if (uiState.isLoading) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 24.dp + Constants.NAVIGATION_BAR_HEIGHT
                ),
                overscrollEffect = null
            ) {
                items(3) {
                    ShimmerHistoryCard()
                }
            }
        } else if (uiState.errorMessage != null) {
            EmptyBox(
                modifier = Modifier.fillMaxSize(),
                text = uiState.errorMessage
            )
        } else if (uiState.tickets.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 24.dp + Constants.NAVIGATION_BAR_HEIGHT
                ),
                overscrollEffect = null
            ) {
                items(uiState.tickets.size) { index ->
                    val history = uiState.tickets[index]
                    HistoryCard(
                        ticket = history,
                        onCLick = onNavigateToTicketDetail
                    )
                }
            }
        } else {
            EmptyBox(
                modifier = Modifier.fillMaxSize(),
                text = stringResource(R.string.ticket_list_text_no_ticket)
            )
        }
    }
}