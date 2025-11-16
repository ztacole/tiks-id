package com.zetta.tiksid.ui.screen.ticket.detail

import com.zetta.tiksid.data.model.TicketDetail

data class TicketDetailUiState(
    val isLoading: Boolean = false,
    val ticket: TicketDetail? = null,
    val errorMessages: String? = null
)
