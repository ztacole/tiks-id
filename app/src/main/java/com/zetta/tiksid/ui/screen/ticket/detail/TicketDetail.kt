package com.zetta.tiksid.ui.screen.ticket.detail

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun TicketDetail(
    viewModel: TicketDetailViewModel,
    modifier: Modifier = Modifier
) {
    TicketDetailScreen(
        uiState = viewModel.uiState,
        modifier = modifier
    )
}