package com.zetta.tiksid.ui.screen.movie.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class MovieDetailViewModel(

): ViewModel() {
    var uiState by mutableStateOf(MovieDetailUiState())
        private set


}