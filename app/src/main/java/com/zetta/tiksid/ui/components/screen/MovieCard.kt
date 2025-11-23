package com.zetta.tiksid.ui.components.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.zetta.tiksid.R
import com.zetta.tiksid.data.model.Movie
import com.zetta.tiksid.ui.theme.AppTheme
import com.zetta.tiksid.utils.shimmerLoading

@Composable
fun MovieCard(
    movie: Movie,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    titleStyle: TextStyle = MaterialTheme.typography.titleMedium,
    overviewStyle: TextStyle = MaterialTheme.typography.bodyMedium
) {
    Column(
        modifier = modifier
            .wrapContentSize()
            .clip(MaterialTheme.shapes.medium)
            .clickable(
                enabled = true,
                onClickLabel = "Open movie detail"
            ) { onClick(movie.id) }
            .padding(8.dp)
    ) {
        SubcomposeAsyncImage(
            model = movie.poster,
            contentDescription = null,
            loading = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(2 / 3f)
                        .clip(MaterialTheme.shapes.medium)
                        .shimmerLoading()
                )
            },
            error = {
                Image(
                    painter = painterResource(id = R.drawable.error_image_placeholder),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(2 / 3f)
                        .clip(MaterialTheme.shapes.medium),
                    contentScale = ContentScale.Crop
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(2 / 3f)
                .clip(MaterialTheme.shapes.medium),
            contentScale = ContentScale.Crop
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = movie.title,
            style = titleStyle,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = pluralStringResource(R.plurals.text_movie_overview, movie.duration, movie.genre[0], movie.duration),
            style = overviewStyle.copy(color = Color.Gray),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun ShimmerMovieCard(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(8.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
                .aspectRatio(2 / 3f)
                .clip(MaterialTheme.shapes.medium)
                .shimmerLoading()
        )
        Spacer(Modifier.height(24.dp))
        Box(
            modifier = Modifier.width(120.dp)
                .height(18.dp)
                .clip(CircleShape)
                .shimmerLoading()
        )
        Spacer(Modifier.height(8.dp))
        Box(
            modifier = Modifier.width(90.dp)
                .height(12.dp)
                .clip(CircleShape)
                .shimmerLoading()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MovieCardPreview() {
    AppTheme {
        Surface {
            MovieCard(
                movie = Movie(
                    id = 1,
                    title = "Deadpool & Wolverine",
                    duration = 128,
                    poster = "",
                    genre = listOf("Action")
                ),
                onClick = {},
                modifier = Modifier.padding(12.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ShimmerMovieCardPreview() {
    AppTheme {
        Surface {
            ShimmerMovieCard(
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}