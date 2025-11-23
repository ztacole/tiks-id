package com.zetta.tiksid.ui.screen.ticket.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zetta.tiksid.data.model.Movie
import com.zetta.tiksid.data.model.Ticket
import com.zetta.tiksid.data.repository.TicketRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TicketListViewModel(
    private val repository: TicketRepository
): ViewModel() {
    var uiState by mutableStateOf(TicketListUiState())
        private set

    init {
        loadTickets()
    }

    fun loadTickets() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)

            repository.getMyBookings()
                .onSuccess {
                    uiState = uiState.copy(
                        tickets = it,
                        isLoading = false
                    )
                }
                .onFailure {
                    uiState = uiState.copy(
                        isLoading = false,
                        errorMessage = it.message
                    )
                }
        }
    }
}