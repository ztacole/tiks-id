package com.zetta.tiksid.ui.screen.movie.detail

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.zetta.tiksid.R
import com.zetta.tiksid.data.model.DateSchedule
import com.zetta.tiksid.data.model.TimeSchedule
import com.zetta.tiksid.ui.components.AppButton
import com.zetta.tiksid.ui.components.AppDialog
import com.zetta.tiksid.ui.components.AppDropdown
import com.zetta.tiksid.ui.components.screen.GenreChip
import com.zetta.tiksid.ui.components.screen.SeatItem
import com.zetta.tiksid.ui.components.screen.SelectableChip
import com.zetta.tiksid.utils.formatRupiah
import com.zetta.tiksid.utils.shimmerLoading

@Composable
fun BookingScreen(
    uiState: BookingUiState,
    paymentState: PaymentState,
    onNavigateToTicketList: () -> Unit,
    onSelectTheater: (Int) -> Unit,
    onSelectDate: (String) -> Unit,
    onSelectTime: (TimeSchedule) -> Unit,
    onSelectSeat: (String) -> Unit,
    onShowConfirmationDialog: () -> Unit,
    onDismissConfirmationDialog: () -> Unit,
    onProceedToPayment: () -> Unit,
    onClearPaymentError: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lazyListState = rememberLazyListState()

    LaunchedEffect(paymentState.paymentSucceed) {
        if (paymentState.paymentSucceed) onNavigateToTicketList()
    }

    LaunchedEffect(uiState.toastMessage) {
        uiState.toastMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    when {
        uiState.errorMessage != null -> {
            Box(Modifier.fillMaxSize()) {
                Text(
                    text = uiState.errorMessage,
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }
        }
        uiState.isLoading -> {
            Box(Modifier.fillMaxSize()) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
        uiState.movie == null -> {
            Box(modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface))
        }
        else -> {
            Box(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    modifier = modifier.fillMaxSize(),
                    overscrollEffect = null,
                    verticalArrangement = Arrangement.spacedBy(32.dp),
                    contentPadding = PaddingValues(bottom = 112.dp),
                    state = lazyListState
                ) {
                    item {
                        PosterSection(uiState.movie)
                    }
                    if (uiState.theaters.isEmpty()) {
                        item { StayTunedCard(Modifier.padding(horizontal = 24.dp)) }
                    } else {
                        item {
                            TheaterSelector(
                                theaters = uiState.theaters,
                                selectedTheater = uiState.selectedTheater,
                                onSelectTheater = onSelectTheater
                            )
                        }

                        item {
                            DateSelector(
                                availableDates = uiState.availableDates,
                                selectedDate = uiState.selectedDate,
                                onSelectDate = onSelectDate
                            )
                        }

                        item {
                            AnimatedVisibility(
                                visible = uiState.selectedDate != null,
                                enter = fadeIn(tween(250)),
                                exit = fadeOut(tween(250))
                            ) {
                                Column {
                                    TimeSelector(
                                        availableShowTimes = uiState.availableShowTimes,
                                        selectedShowTime = uiState.selectedShowTime,
                                        onSelectTime
                                    )
                                }
                            }
                        }

                        item {
                            AnimatedVisibility(
                                visible = uiState.selectedShowTime != null,
                                enter = fadeIn(tween(250)),
                                exit = fadeOut(tween(250))
                            ) {
                                Column {
                                    SeatSelectionSection(
                                        seats = uiState.seats,
                                        onSelectSeat = onSelectSeat
                                    )
                                    Spacer(Modifier.height(16.dp))
                                    Text(
                                        text = stringResource(R.string.detail_text_cinema_screen),
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Center,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color.Gray
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Canvas(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(24.dp),
                                    ) {
                                        val path = Path()
                                        path.moveTo(24.dp.toPx(), -size.height / 2)
                                        path.quadraticTo(
                                            size.width / 2,
                                            size.height,
                                            size.width - 24.dp.toPx(),
                                            -size.height / 2
                                        )
                                        drawPath(
                                            path = path,
                                            color = Color.Gray,
                                            style = Stroke(width = 4.dp.toPx())
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Floating payment button
                if (uiState.theaters.isNotEmpty()) {
                    AnimatedVisibility(
                        visible = uiState.selectedSeats.isNotEmpty(),
                        enter = fadeIn(tween(250)),
                        exit = fadeOut(tween(250)),
                        modifier = Modifier.align(Alignment.BottomCenter)
                    ) {
                        PaymentButton(
                            total = uiState.totalPrice,
                            onClick = onShowConfirmationDialog,
                            modifier = Modifier
                                .padding(start = 24.dp, end = 24.dp, bottom = 24.dp)
                                .align(Alignment.BottomCenter)
                        )
                    }
                }
            }
        }
    }

    if (paymentState.confirmPayment) {
        AppDialog(
            show = true,
            onDismiss = {  },
            dismissOnBackPress = false,
            dismissOnOutsideClick = false,
            title = stringResource(R.string.detail_dialog_confirm_payment),
            content = {
                Text(
                    text = pluralStringResource(R.plurals.detail_dialog_confirm_payment, uiState.selectedSeats.size, uiState.selectedSeats.size),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            },
            confirmButton = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    AppButton(
                        text = stringResource(R.string.text_cancel),
                        onClick = onDismissConfirmationDialog,
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                    Spacer(Modifier.width(8.dp))
                    AppButton(
                        text = stringResource(R.string.text_confirm),
                        onClick = onProceedToPayment
                    )
                }
            }
        )
    }

    if (paymentState.isLoading) {
        AppDialog(
            show = true,
            onDismiss = { },
            content = { CircularProgressIndicator(modifier = Modifier.padding(top = 32.dp)) }
        )
    }

    paymentState.errorMessage?.let {
        AppDialog(
            show = true,
            onDismiss = {},
            dismissOnBackPress = false,
            dismissOnOutsideClick = false,
            title = stringResource(R.string.detail_dialog_failed_to_payment),
            content = {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            },
            confirmButton = {
                AppButton(
                    text = stringResource(R.string.text_ok),
                    onClick = onClearPaymentError
                )
            }
        )
    }
}

@Composable
fun PosterSection(
    movie: MovieDetail
) {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        PosterImage(movie.poster)
        GradientOverlay(modifier = Modifier.matchParentSize())
        PosterContent(
            title = movie.title,
            duration = movie.duration,
            releaseDate = movie.releaseDate,
            genres = movie.genres,
            description = movie.description,
            modifier = Modifier.align(Alignment.BottomStart)
        )
    }
}

@Composable
private fun PosterImage(
    poster: String
) {
    SubcomposeAsyncImage(
        model = poster,
        contentDescription = null,
        loading = {
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(480.dp)
                .shimmerLoading())
        },
        error = {
            Image(
                painter = painterResource(id = R.drawable.error_image_placeholder),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth().height(480.dp),
                contentScale = ContentScale.Crop
            )
        },
        modifier = Modifier.fillMaxWidth().height(480.dp),
        contentScale = ContentScale.FillWidth
    )
}

@Composable
private fun GradientOverlay(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        Color.Transparent,
                        MaterialTheme.colorScheme.surface,
                    )
                )
            )
    )
}

@Composable
private fun PosterContent(
    title: String,
    duration: Int,
    releaseDate: String,
    genres: List<String>,
    description: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Spacer(Modifier.weight(1f))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = pluralStringResource(
                    R.plurals.text_movie_overview,
                    duration,
                    releaseDate,
                    duration
                ),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.LightGray,
            )
            Spacer(Modifier.height(12.dp))
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                val items = genres
                items.forEach { genre ->
                    GenreChip(genre)
                }
            }
            Spacer(Modifier.height(12.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

@Composable
private fun TheaterSelector(
    theaters: List<Theater>,
    selectedTheater: Theater?,
    onSelectTheater: (Int) -> Unit
) {
    Text(
        text = stringResource(R.string.detail_text_theater),
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(horizontal = 24.dp)
    )
    Spacer(Modifier.height(8.dp))
    AppDropdown(
        items = theaters.flatMap { listOf(it.name) },
        selectedItem = selectedTheater?.name ?: "",
        onItemSelected = {
            onSelectTheater(
                theaters.find { theater -> theater.name == it }?.id ?: 0
            )
        },
        modifier = Modifier.padding(horizontal = 24.dp)
    )
}

@Composable
private fun DateSelector(
    availableDates: List<DateSchedule>,
    selectedDate: String?,
    onSelectDate: (String) -> Unit
) {
    Text(
        text = stringResource(R.string.detail_text_date),
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(horizontal = 24.dp)
    )
    Spacer(Modifier.height(8.dp))
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 24.dp)
    ) {
        items(availableDates) { date ->
            val splitDate = date.date.split(" ")
            val day = splitDate[0]
            val month = splitDate[1]
            val year = splitDate[2]
            val formattedDate = "$day $month\n$year"
            SelectableChip(
                text = formattedDate,
                isSelected = selectedDate == date.date,
                onClick = { onSelectDate(date.date) }
            )
        }
    }
}

@Composable
private fun TimeSelector(
    availableShowTimes: List<TimeSchedule>,
    selectedShowTime: ShowTime?,
    onSelectTime: (TimeSchedule) -> Unit
) {
    Text(
        text = stringResource(R.string.detail_text_available_time),
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(horizontal = 24.dp)
    )
    Spacer(Modifier.height(8.dp))
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 24.dp)
    ) {
        items(availableShowTimes) { showTime ->
            SelectableChip(
                text = showTime.time,
                isSelected = selectedShowTime?.time == showTime.time,
                onClick = { onSelectTime(showTime) }
            )
        }
    }
}

@Composable
private fun SeatSelectionSection(
    seats: Map<String, Map<Int, List<Seat>>>,
    onSelectSeat: (String) -> Unit
) {
    Text(
        text = stringResource(R.string.detail_text_choose_seat),
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(horizontal = 24.dp)
    )
    Spacer(Modifier.height(8.dp))
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(horizontal = 24.dp),
        overscrollEffect = null
    ) {
        items(seats.keys.toList(), key = { it }) { sectionId ->

            val columnMap = seats[sectionId]!!

            Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {

                columnMap.forEach { (_, seatsInColumn) ->
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        seatsInColumn.forEach { seat ->
                            SeatItem(
                                seat = seat,
                                status = seat.status,
                                onSeatSelected = onSelectSeat
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PaymentButton(
    total: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clickable(enabled = total > 0, onClick = onClick)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary, CircleShape)
            .padding(horizontal = 24.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.widthIn(max = 248.dp)) {
            Text(
                text = pluralStringResource(R.plurals.detail_button_buy_ticket, total),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Text(
                text = formatRupiah(total),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
        Icon(
            imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
            contentDescription = null,
            modifier = Modifier.size(28.dp),
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
private fun StayTunedCard(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
            .padding(horizontal = 24.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(R.string.detail_text_stay_tuned),
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.widthIn(max = 248.dp)
        )
        Icon(
            imageVector = Icons.Outlined.Info,
            contentDescription = null,
            modifier = Modifier.size(28.dp)
        )
    }
}