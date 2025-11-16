package com.zetta.tiksid.ui.screen.ticket.list

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.koin.androidx.compose.koinViewModel

@Composable
fun TicketList(
    onNavigateToTicketDetail: (Int) -> Unit,
) {
    val viewModel: TicketListViewModel = koinViewModel()

    TicketListScreen(
        uiState = viewModel.uiState,
        onNavigateToTicketDetail = onNavigateToTicketDetail,
        modifier = Modifier
    )
}