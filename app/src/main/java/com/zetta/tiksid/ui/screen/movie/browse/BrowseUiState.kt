package com.zetta.tiksid.ui.screen.movie.browse

import com.zetta.tiksid.data.model.Movie

data class BrowseUiState(
    val isLoading: Boolean = false,
    val movies: List<Movie> = emptyList(),
    val errorMessage: String? = null
)
