package com.zetta.tiksid.ui.screen.ticket.detail

import com.zetta.tiksid.data.model.Ticket

data class TicketDetailUiState(
    val isLoading: Boolean = false,
    val ticket: Ticket? = null,
    val errorMessages: String? = null
)
