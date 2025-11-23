package com.zetta.tiksid.ui.screen.movie.browse

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zetta.tiksid.data.repository.MovieRepository
import kotlinx.coroutines.launch

class BrowseViewModel(
    private val repository: MovieRepository
): ViewModel() {
    var uiState by mutableStateOf(BrowseUiState())
        private set

    private var currentPage = 1
    private val pageSize = 16

    init {
        loadMovies()
    }

    fun loadMovies() {
        if (uiState.isLoadingMore || uiState.endReached) return
        uiState = uiState.copy(isLoadingMore = true)

        viewModelScope.launch {
            if (currentPage == 1) {
                uiState = uiState.copy(isLoading = true)
            }

            repository.getMovies(page = currentPage, limit = pageSize)
                .onSuccess {
                    val newData = it.data
                    currentPage = it.meta.page

                    uiState = uiState.copy(
                        movies = uiState.movies + newData,
                        isLoading = false,
                        isRefreshing = false,
                        isLoadingMore = false,
                        endReached = newData.isEmpty()
                    )
                }
                .onFailure {
                    uiState = uiState.copy(
                        isLoading = false,
                        isRefreshing = false,
                        isLoadingMore = false,
                        errorMessage = it.message
                    )
                }

            if (!uiState.endReached) currentPage++
        }
    }

    fun refreshMovies() {
        currentPage = 1
        uiState = uiState.copy(
            movies = emptyList(),
            isRefreshing = true,
            endReached = false
        )
        loadMovies()
    }
}