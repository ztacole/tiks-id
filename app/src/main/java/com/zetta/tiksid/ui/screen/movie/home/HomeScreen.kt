package com.zetta.tiksid.ui.screen.movie.home

import android.widget.Toast
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zetta.tiksid.R
import com.zetta.tiksid.data.model.Movie
import com.zetta.tiksid.ui.components.screen.MovieCard
import com.zetta.tiksid.ui.components.screen.ShimmerMovieCard
import com.zetta.tiksid.ui.theme.AppTheme
import com.zetta.tiksid.utils.Constants

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onNavigateToMovieDetail: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    LaunchedEffect(uiState.errorMessage) {
        if (uiState.errorMessage != null)
            Toast.makeText(context, uiState.errorMessage, Toast.LENGTH_SHORT).show()
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        overscrollEffect = null
    ) {
        item {
            Spacer(Modifier.padding(top = 24.dp))
            Text(
                text = stringResource(R.string.home_text_featured_movie),
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp),
                modifier = Modifier.padding(horizontal = 24.dp)
            )
            Spacer(Modifier.height(8.dp))
        }
        item {
            if (uiState.isLoading) {
                ShimmerMovieCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
            } else {
                MovieCard(
                    movie = uiState.featuredMovie!!,
                    onClick = onNavigateToMovieDetail,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    titleStyle = MaterialTheme.typography.titleLarge,
                    overviewStyle = MaterialTheme.typography.bodyLarge
                )
            }
            Spacer(Modifier.height(28.dp))
        }
        item {
            Text(
                text = stringResource(R.string.home_text_new_titles),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
            Spacer(Modifier.height(4.dp))
            LazyRow(
                overscrollEffect = null,
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 24.dp + Constants.NAVIGATION_BAR_HEIGHT)
            ) {
                if (uiState.isLoading) {
                    items(3) {
                        ShimmerMovieCard(modifier = Modifier.width(170.dp))
                    }
                } else {
                    items(uiState.movies.size) {
                        val movie = uiState.movies[it]

                        MovieCard(
                            movie = movie,
                            onClick = onNavigateToMovieDetail,
                            modifier = Modifier.width(170.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun HomeScreenPrev() {
    AppTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            HomeScreen(
                uiState = HomeUiState(
                    featuredMovie = Movie(
                        id = 1,
                        title = "Deadpool & Wolverine",
                        duration = 128,
                        poster = "",
                        genre = "Action"
                    ),
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