package com.zetta.tiksid.ui.screen.ticket.list

import com.zetta.tiksid.data.model.Ticket

data class TicketListUiState(
    val isLoading: Boolean = false,
    val tickets: List<Ticket> = emptyList(),
    val errorMessage: String? = null
)
