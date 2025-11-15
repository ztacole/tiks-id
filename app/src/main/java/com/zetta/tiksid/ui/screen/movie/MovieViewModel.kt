package com.zetta.tiksid.ui.screen.movie

import androidx.lifecycle.ViewModel
import com.zetta.tiksid.data.model.Movie

class MovieViewModel(

): ViewModel() {

    data class HomeUiState(
        val isLoading: Boolean = false,
        val featuredMovie: Movie? = null,
        val movies: List<Movie> = emptyList(),
        val errorMessage: String? = null,
    )
}