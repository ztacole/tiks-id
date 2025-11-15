package com.zetta.tiksid.ui.screen.movie.home

import com.zetta.tiksid.data.model.Movie

data class HomeUiState(
    val isLoading: Boolean = false,
    val featuredMovie: Movie? = null,
    val movies: List<Movie> = emptyList(),
    val errorMessage: String? = null,
)
