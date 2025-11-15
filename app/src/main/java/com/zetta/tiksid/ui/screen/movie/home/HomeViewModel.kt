package com.zetta.tiksid.ui.screen.movie.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zetta.tiksid.data.model.Movie
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeViewModel(

): ViewModel() {
    var uiState by mutableStateOf(HomeUiState())
        private set

    private val featuredMovie = Movie(
        id = 1,
        title = "Deadpool & Wolverine",
        poster = "",
        genre = "Action",
        duration = 128
    )
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
                featuredMovie = featuredMovie,
                movies = movies,
                isLoading = false
            )
        }
    }
}