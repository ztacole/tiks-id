package com.zetta.tiksid.ui.screen.ticket.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zetta.tiksid.data.repository.TicketRepository
import kotlinx.coroutines.launch

class TicketDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: TicketRepository
): ViewModel() {
    private val ticketId: Int = savedStateHandle["ticketId"] ?: 0

    var uiState by mutableStateOf(TicketDetailUiState())
        private set

    init {
        loadTicket(ticketId)
    }

    private fun loadTicket(id: Int) {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)

            repository.getMyBookings(id)
                .onSuccess {
                    uiState = uiState.copy(
                        ticket = it,
                        isLoading = false
                    )
                }
                .onFailure {
                    uiState = uiState.copy(
                        isLoading = false,
                        errorMessages = it.message
                    )
                }
        }
    }
}