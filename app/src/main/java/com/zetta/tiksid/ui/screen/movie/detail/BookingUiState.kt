package com.zetta.tiksid.ui.screen.movie.detail

import androidx.compose.runtime.Stable
import com.zetta.tiksid.data.model.DateSchedule
import com.zetta.tiksid.data.model.TimeSchedule

data class BookingUiState(
    val isLoading: Boolean = false,
    val movie: MovieDetail? = null,
    val selectedTheater: Theater? = null,
    val theaters: List<Theater> = emptyList(),
    val selectedDate: String? = null,
    val availableDates: List<DateSchedule> = emptyList(),
    val selectedShowTime: ShowTime? = null,
    val availableShowTimes: List<TimeSchedule> = emptyList(),
    val seats: Map<String, Map<Int, List<Seat>>> = emptyMap(),
    val selectedSeats: List<Seat> = emptyList(),
    val pricePerSeat: Int = 0,
    val totalPrice: Int = 0,
    val errorMessage: String? = null,
    val toastMessage: String? = null
)

data class PaymentState(
    val confirmPayment: Boolean = false,
    val isLoading: Boolean = false,
    val paymentSucceed: Boolean = false,
    val errorMessage: String? = null
)

data class MovieDetail(
    val id: Int,
    val title: String,
    val description: String,
    val poster: String,
    val releaseDate: String,
    val duration: Int,
    val genres: List<String>,
)

@Stable
data class Theater(
    val id: Int,
    val name: String,
    val sectionCount: Int,
    val rowCount: Int,
    val columnCount: Int,
    val sections: List<SeatSection>
)

data class SeatSection(
    val id: String,
    val name: String,
    val rowStart: Int,
    val rowEnd: Int,
    val columns: List<Int>
)

@Stable
data class Seat(
    val id: String,
    val row: String,
    val column: Int,
    val sectionId: String,
    val displayLabel: String,
    val status: SeatStatus = SeatStatus.AVAILABLE
)

enum class SeatStatus {
    AVAILABLE,
    SELECTED,
    UNAVAILABLE
}

data class ShowTime(
    val scheduleId: Int,
    val time: String,
    val date: String,
    val filledSeats: List<String>,
    val price: Int
)