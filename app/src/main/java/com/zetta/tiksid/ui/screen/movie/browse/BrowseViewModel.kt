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

    private var currentPage = 1
    private val pageSize = 20

    private val allMovies = List(100) {
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
        if (uiState.isLoadingMore || uiState.endReached) return
        uiState = uiState.copy(isLoadingMore = true)

        viewModelScope.launch {
            if (currentPage == 1) {
                uiState = uiState.copy(isLoading = true)
            }

            delay(2000) // simulasi API

            val start = (currentPage - 1) * pageSize
            val end = minOf(start + pageSize, allMovies.size)

            val newData = allMovies.slice(start until end)

            uiState = uiState.copy(endReached = newData.isEmpty())

            uiState = uiState.copy(
                movies = uiState.movies + newData,
                isLoading = false,
                isRefreshing = false,
                isLoadingMore = false
            )

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