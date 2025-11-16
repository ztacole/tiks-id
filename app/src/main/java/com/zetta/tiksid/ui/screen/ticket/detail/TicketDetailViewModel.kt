package com.zetta.tiksid.ui.screen.ticket.detail

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zetta.tiksid.data.model.Movie
import com.zetta.tiksid.data.model.TicketDetail
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TicketDetailViewModel(
    private val savedStateHandle: SavedStateHandle,
): ViewModel() {
    private val ticketId: Int = savedStateHandle["ticketId"] ?: 0

    val ticket = TicketDetail(
        id = 1,
        movie = Movie(
            id = 1,
            title = "Deadpool & Wolverine",
            genre = "Action",
            duration = 128,
            poster = ""
        ),
        theater = "Diana Theater",
        schedule = "28 Oct 2024 08:00",
        seats = List(5) { "A${it + 1}" }
    )

    var uiState by mutableStateOf(TicketDetailUiState())
        private set

    init {
        loadTicket(ticketId)
    }

    private fun loadTicket(id: Int) {
        viewModelScope.launch {
            Log.d("TAG", "ID: $id")
            uiState = uiState.copy(isLoading = true)
            delay(2000)

            uiState = uiState.copy(
                ticket = ticket,
                isLoading = false
            )
        }
    }
}