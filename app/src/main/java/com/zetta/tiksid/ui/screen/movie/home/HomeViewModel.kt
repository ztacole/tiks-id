package com.zetta.tiksid.ui.screen.movie.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zetta.tiksid.data.repository.MovieRepository
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: MovieRepository
): ViewModel() {
    var uiState by mutableStateOf(HomeUiState())
        private set

    init {
        loadMovies()
    }

    fun loadMovies() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)

            repository.getMovies(page = 1, limit = 12)
                .onSuccess {
                    val recentlyReleasedMovie = it.data.firstOrNull()
                    val movies = it.data.drop(1)
                    uiState = uiState.copy(
                        recentlyReleasedMovie = recentlyReleasedMovie,
                        movies = movies,
                        isLoading = false
                    )
                }
                .onFailure {
                    uiState = uiState.copy(
                        errorMessage = it.message,
                        isLoading = false
                    )
                }
        }
    }
}