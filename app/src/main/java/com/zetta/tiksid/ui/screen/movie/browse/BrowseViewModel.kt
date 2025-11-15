package com.zetta.tiksid.ui.screen.movie.browse

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zetta.tiksid.data.model.Movie
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BrowseViewModel(

): ViewModel() {
    var uiState by mutableStateOf(BrowseUiState())
        private set

    private val movies = List(20) {
        Movie(
            id = it + 1,
            title = "Movie ${it + 1}",
            poster = "",
            genre = "Action",
            duration = 128
        )
    }

    init {
        loadMovies()
    }

    fun loadMovies() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)
            delay(2000)

            uiState = uiState.copy(
                movies = movies,
                isLoading = false
            )
        }
    }
}