package com.zetta.tiksid.ui.screen.movie.browse

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zetta.tiksid.R
import com.zetta.tiksid.data.model.Movie
import com.zetta.tiksid.ui.components.screen.MovieCard
import com.zetta.tiksid.ui.components.screen.ShimmerMovieCard
import com.zetta.tiksid.ui.theme.AppTheme

@Composable
fun BrowseScreen(
    uiState: BrowseUiState,
    onNavigateToMovieDetail: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    LaunchedEffect(uiState.errorMessage) {
        if (uiState.errorMessage != null)
            Toast.makeText(context, uiState.errorMessage, Toast.LENGTH_SHORT).show()
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
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(0.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            if (uiState.isLoading) {
                items(4) {
                    ShimmerMovieCard(
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            } else {
                items(uiState.movies.size) {
                    val movie = uiState.movies[it]

                    MovieCard(
                        movie = movie,
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { onNavigateToMovieDetail(movie.id.toString()) }
                    )
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
                            genre = "Action"
                        )
                    }
                ),
                onNavigateToMovieDetail = {}
            )
        }
    }
}