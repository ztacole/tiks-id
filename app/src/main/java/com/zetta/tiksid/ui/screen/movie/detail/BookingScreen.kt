package com.zetta.tiksid.ui.screen.movie.detail

import android.widget.Toast
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zetta.tiksid.R
import com.zetta.tiksid.data.model.DateSchedule
import com.zetta.tiksid.data.model.TimeSchedule
import com.zetta.tiksid.ui.components.AppButton
import com.zetta.tiksid.ui.components.AppDialog
import com.zetta.tiksid.ui.components.AppDropdown
import com.zetta.tiksid.ui.components.screen.GenreChip
import com.zetta.tiksid.ui.components.screen.SeatItem
import com.zetta.tiksid.ui.components.screen.SelectableChip
import com.zetta.tiksid.ui.theme.AppTheme
import com.zetta.tiksid.utils.formatDateToYear
import com.zetta.tiksid.utils.formatRupiah

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

    LaunchedEffect(paymentState.paymentSucceed) {
        if (paymentState.paymentSucceed) onNavigateToTicketList()
    }

    LaunchedEffect(uiState.toastMessage) {
        uiState.toastMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    if (uiState.errorMessage != null) {
        Box(Modifier.fillMaxSize()) {
            Text(
                text = uiState.errorMessage,
                modifier = Modifier.align(Alignment.Center),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
    }

    if (uiState.isLoading) {
        Box(Modifier.fillMaxSize()) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.colorScheme.primary,
            )
        }
    } else {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            overscrollEffect = null,
            verticalArrangement = Arrangement.spacedBy(32.dp),
            contentPadding = PaddingValues(bottom = 48.dp)
        ) {
            item {
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.dummy_poster),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.FillWidth
                    )
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(
                                brush = Brush.verticalGradient(
                                    listOf(
                                        Color.Transparent,
                                        MaterialTheme.colorScheme.surface,
                                    )
                                )
                            )
                    )
                    Column(
                        modifier = Modifier.matchParentSize()
                    ) {
                        Spacer(Modifier.weight(1f))
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp)
                        ) {
                            Text(
                                text = uiState.movie?.title ?: "",
                                style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = pluralStringResource(
                                    R.plurals.text_movie_overview,
                                    uiState.movie?.duration ?: 0,
                                    uiState.movie?.releaseDate ?: "",
                                    uiState.movie?.duration ?: 0
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
                                val items = uiState.movie?.genres ?: emptyList()
                                items.forEach { genre ->
                                    GenreChip(genre)
                                }
                            }
                            Spacer(Modifier.height(12.dp))
                            Text(
                                text = uiState.movie?.description ?: "",
                                style = MaterialTheme.typography.bodySmall,
                            )
                        }
                    }
                }
            }
            if (uiState.theaters.isEmpty()) {
                item { StayTunedCard(Modifier.padding(horizontal = 24.dp)) }
            } else {
                item {
                    Text(
                        text = stringResource(R.string.detail_text_theater),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                    Spacer(Modifier.height(8.dp))
                    AppDropdown(
                        items = uiState.theaters.flatMap { listOf(it.name) },
                        selectedItem = uiState.selectedTheater?.name ?: "",
                        onItemSelected = {
                            onSelectTheater(
                                uiState.theaters.find { theater -> theater.name == it }?.id ?: 0
                            )
                        },
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                }

                item {
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
                        items(uiState.availableDates) { date ->
                            SelectableChip(
                                text = date.date,
                                isSelected = uiState.selectedDate == date.date,
                                onClick = { onSelectDate(date.date) }
                            )
                        }
                    }
                }

                item {
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
                        items(uiState.availableShowTimes) { showTime ->
                            SelectableChip(
                                text = showTime.time,
                                isSelected = uiState.selectedShowTime?.time == showTime.time,
                                onClick = { onSelectTime(showTime) }
                            )
                        }
                    }
                }

                item {
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
                        items(uiState.seats.keys.toList(), key = { it }) { sectionId ->

                            val columnMap = uiState.seats[sectionId]!!

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

                item {
                    PaymentButton(
                        total = uiState.totalPrice,
                        onClick = onShowConfirmationDialog,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
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
fun PaymentButton(
    total: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (total > 0) MaterialTheme.colorScheme.primary
    else MaterialTheme.colorScheme.primary.copy(alpha = 0.65f)

    val textColor = if (total > 0) MaterialTheme.colorScheme.onPrimary
    else MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.65f)

    Row(
        modifier = modifier
            .clickable(enabled = total > 0, onClick = onClick)
            .fillMaxWidth()
            .background(backgroundColor, CircleShape)
            .padding(horizontal = 24.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.widthIn(max = 248.dp)) {
            Text(
                text = pluralStringResource(R.plurals.detail_button_buy_ticket, total),
                style = MaterialTheme.typography.bodySmall,
                color = textColor
            )
            Text(
                text = formatRupiah(total),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = textColor
            )
        }
        Icon(
            imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
            contentDescription = null,
            modifier = Modifier.size(28.dp),
            tint = textColor
        )
    }
}

@Composable
fun StayTunedCard(modifier: Modifier = Modifier) {
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