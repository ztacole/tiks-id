package com.zetta.tiksid.ui.screen.ticket.list

import com.zetta.tiksid.data.model.History

data class TicketListUiState(
    val isLoading: Boolean = false,
    val histories: List<History> = emptyList(),
    val errorMessage: String? = null
)
