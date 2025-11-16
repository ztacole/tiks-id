package com.zetta.tiksid.ui.screen.movie.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.koin.androidx.compose.koinViewModel

@Composable
fun Home(
    onNavigateToMovieDetail: (Int) -> Unit,
) {
    val viewModel: HomeViewModel = koinViewModel()

    HomeScreen(
        uiState = viewModel.uiState,
        onNavigateToMovieDetail = onNavigateToMovieDetail,
        modifier = Modifier,
    )
}