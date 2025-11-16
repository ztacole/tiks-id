package com.zetta.tiksid.ui.screen.ticket.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zetta.tiksid.data.model.Movie
import com.zetta.tiksid.data.model.History
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TicketListViewModel(

): ViewModel() {
    var uiState by mutableStateOf(TicketListUiState())
        private set

    val histories = List(12) {
        History(
            id = it,
            movie = Movie(
                id = it + 1,
                title = "Movie ${it + 1}",
                poster = "",
                genre = "Action",
                duration = 128
            ),
            schedule = "28 Oct 2024 08:00",
            seats = List(5) { "A${it + 1}" },
            totalPrice = 200000
        )
    }

    init {
        loadTickets()
    }

    fun loadTickets() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)
            delay(2000)

            uiState = uiState.copy(
                isLoading = false,
                histories = histories
            )
        }
    }
}