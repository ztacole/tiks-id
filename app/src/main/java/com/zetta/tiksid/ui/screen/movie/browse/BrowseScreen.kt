package com.zetta.tiksid.ui.screen.movie.browse

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zetta.tiksid.R
import com.zetta.tiksid.data.model.Movie
import com.zetta.tiksid.ui.components.CompassRefreshIndicator
import com.zetta.tiksid.ui.components.screen.MovieCard
import com.zetta.tiksid.ui.components.screen.ShimmerMovieCard
import com.zetta.tiksid.ui.theme.AppTheme
import com.zetta.tiksid.utils.Constants

@Composable
fun BrowseScreen(
    uiState: BrowseUiState,
    onFetchMore: () -> Unit,
    onRefresh: () -> Unit,
    onNavigateToMovieDetail: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val gridState = rememberLazyGridState()
    val pullRefreshState = rememberPullToRefreshState()

    val refreshFraction = { pullRefreshState.distanceFraction.coerceIn(0f, 1f) }

    LaunchedEffect(uiState.errorMessage) {
        if (uiState.errorMessage != null)
            Toast.makeText(context, uiState.errorMessage, Toast.LENGTH_SHORT).show()
    }

    val shouldFetchNextPage: Boolean by remember {
        derivedStateOf {
            val lastVisibleItem = gridState.layoutInfo.visibleItemsInfo.lastOrNull()
                ?: return@derivedStateOf false
            lastVisibleItem.index == gridState.layoutInfo.totalItemsCount - 3
        }
    }

    LaunchedEffect(shouldFetchNextPage) {
        if (shouldFetchNextPage)
            onFetchMore()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        Spacer(Modifier.height(24.dp))
        Text(
            text = stringResource(R.string.browse_text_all_movies),
            style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp),
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        Spacer(Modifier.height(8.dp))
        PullToRefreshBox(
            isRefreshing = uiState.isRefreshing,
            onRefresh = onRefresh,
            modifier = Modifier,
            state = pullRefreshState,
            indicator = {
                CompassRefreshIndicator(
                    progress = refreshFraction(),
                    isRefreshing = uiState.isRefreshing,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                )
            }
        ) {
            LazyVerticalGrid(
                state = gridState,
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(bottom = 24.dp + Constants.NAVIGATION_BAR_HEIGHT),
                overscrollEffect = null
            ) {
                if (uiState.isLoading) {
                    items(4) {
                        ShimmerMovieCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .graphicsLayer {
                                    translationY = refreshFraction() * 280f
                                }
                        )
                    }
                } else {
                    items(uiState.movies, key = { it.id }) { movie ->
                        MovieCard(
                            movie = movie,
                            modifier = Modifier
                                .fillMaxWidth()
                                .graphicsLayer {
                                    translationY = refreshFraction() * 320f
                                },
                            onClick = onNavigateToMovieDetail
                        )
                    }

                    if (uiState.isLoadingMore) {
                        item(span = { GridItemSpan(maxLineSpan) }) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 24.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun BrowseScreenPrev() {
    AppTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            BrowseScreen(
                uiState = BrowseUiState(
                    movies = List(12) {
                        Movie(
                            id = it + 1,
                            title = "Title ${it + 1}",
                            duration = 128,
                            poster = "",
                            genre = listOf("Action")
                        )
                    }
                ),
                onFetchMore = {},
                onNavigateToMovieDetail = {},
                onRefresh = {}
            )
        }
    }
}