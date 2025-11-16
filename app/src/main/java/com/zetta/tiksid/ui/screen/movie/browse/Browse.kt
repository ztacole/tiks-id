package com.zetta.tiksid.ui.screen.movie.browse

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.koin.androidx.compose.koinViewModel

@Composable
fun Browse(
    onNavigateToMovieDetail: (Int) -> Unit,
) {
    val viewModel: BrowseViewModel = koinViewModel()

    BrowseScreen(
        uiState = viewModel.uiState,
        onNavigateToMovieDetail = onNavigateToMovieDetail,
        modifier = Modifier
    )
}