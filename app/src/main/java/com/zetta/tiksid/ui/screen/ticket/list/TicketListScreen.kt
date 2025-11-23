package com.zetta.tiksid.ui.screen.ticket.list

import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zetta.tiksid.R
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
        if (uiState.errorMessage != null) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = uiState.errorMessage,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.align(Alignment.Center),
                    textAlign = TextAlign.Center
                )
            }
        } else if (uiState.tickets.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = stringResource(R.string.ticket_list_text_no_ticket),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.align(Alignment.Center),
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                overscrollEffect = null,
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 24.dp + Constants.NAVIGATION_BAR_HEIGHT
                )
            ) {
                if (uiState.isLoading) {
                    items(3) {
                        ShimmerHistoryCard()
                    }
                } else {
                    items(uiState.tickets.size) { index ->
                        val history = uiState.tickets[index]

                        HistoryCard(
                            ticket = history,
                            onCLick = onNavigateToTicketDetail
                        )
                    }
                }
            }
        }
    }
}