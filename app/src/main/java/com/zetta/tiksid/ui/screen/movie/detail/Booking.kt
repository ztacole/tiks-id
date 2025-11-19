package com.zetta.tiksid.ui.screen.movie.detail

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun Booking(
    viewModel: BookingViewModel,
    onNavigateToTicketList: () -> Unit,
    modifier: Modifier = Modifier
) {
    BookingScreen(
        uiState = viewModel.uiState,
        paymentState = viewModel.paymentState,
        onNavigateToTicketList = onNavigateToTicketList,
        onSelectTheater = viewModel::selectTheater,
        onSelectDate = viewModel::selectDate,
        onSelectTime = viewModel::selectShowTime,
        onSelectSeat = viewModel::toggleSeatSelection,
        onShowConfirmationDialog = viewModel::showConfirmationDialog,
        onDismissConfirmationDialog = viewModel::dismissConfirmationDialog,
        onProceedToPayment = viewModel::proceedToPayment,
        onClearPaymentError = viewModel::clearPaymentError,
        modifier = modifier
    )
}