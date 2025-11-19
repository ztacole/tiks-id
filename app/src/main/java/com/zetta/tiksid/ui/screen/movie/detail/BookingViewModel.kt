package com.zetta.tiksid.ui.screen.movie.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zetta.tiksid.data.model.DateSchedule
import com.zetta.tiksid.data.model.MovieBooking
import com.zetta.tiksid.data.model.TheaterSchedule
import com.zetta.tiksid.data.model.TimeSchedule
import com.zetta.tiksid.utils.formatDateToYear
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BookingViewModel(
    savedStateHandle: SavedStateHandle,
): ViewModel() {
    private val movieId: Int = savedStateHandle["movieId"] ?: 0

    var uiState by mutableStateOf(BookingUiState())
        private set
    var paymentState by mutableStateOf(PaymentState())
        private set

    private val fakeData = MovieBooking(
        id = 1,
        title = "Deadpool & Wolverine",
        description = "Lorem ipsum dolor sit amet",
        duration = 128,
        releaseDate = "2023-05-15",
        poster = "",
        genres = listOf("Action", "Adventure"),
        availableTheaters = listOf(
            TheaterSchedule(
                theaterId = 1,
                theaterName = "Theater Diamond",
                section = 3,
                row = 12,
                column = 10,
                availableDates = listOf(
                    DateSchedule(
                        date = "Mon\n25 Oct",
                        availableTimes = listOf(
                            TimeSchedule(
                                scheduleId = 1,
                                time = "10:00",
                                filledSeats = listOf("A1", "A2", "B1", "B2"),
                                price = 50000
                            ),
                            TimeSchedule(
                                scheduleId = 2,
                                time = "13:00",
                                filledSeats = listOf("A1", "A2", "B1", "B2"),
                                price = 50000
                            ),
                        )
                    ),
                    DateSchedule(
                        date = "Tue\n26 Oct",
                        availableTimes = listOf(
                            TimeSchedule(
                                scheduleId = 3,
                                time = "10:00",
                                filledSeats = listOf("A1", "A2", "B1", "B2"),
                                price = 50000
                            ),
                       )
                    )
                )
            )
        )
    )

    private var movieScheduleResponse: MovieBooking? = null
    private var currentTheaterSchedule: TheaterSchedule? = null

    init {
        loadBookingData()
    }

    private fun loadBookingData() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)
            delay(2000)

            if (fakeData.availableTheaters.isNotEmpty()) {
                val firstTheater = fakeData.availableTheaters.first()
                currentTheaterSchedule = firstTheater

                val movie = MovieDetail(
                    id = movieId,
                    title = fakeData.title,
                    description = fakeData.description,
                    duration = fakeData.duration,
                    releaseDate = formatDateToYear(fakeData.releaseDate),
                    posterUrl = fakeData.poster,
                    genres = fakeData.genres
                )
                val theater = createTheaterFromResponse(firstTheater)
                val seats = generateSeatsForTheater(theater, emptyList())

                uiState = uiState.copy(
                    movie = movie,
                    theaters = fakeData.availableTheaters.map { createTheaterFromResponse(it) },
                    selectedTheater = theater,
                    availableDates = firstTheater.availableDates,
                    seats = seats,
                    isLoading = false
                )
            } else {
                uiState = uiState.copy(
                    isLoading = false,
                )
            }
//            repository.getMovieSchedules(movieId).fold(
//                onSuccess = { response ->
//                    movieScheduleResponse = response
//
//                    if (response.availableTheaters.isNotEmpty()) {
//                        val firstTheater = response.availableTheaters.first()
//                        currentTheaterSchedule = firstTheater
//
//                        val movie = Movie(
//                            id = movieId,
//                            title = response.title
//                        )
//                        val theater = createTheaterFromResponse(firstTheater)
//                        val seats = generateSeatsForTheater(theater, emptyList())
//
//                        uiState = uiState.copy(
//                            movie = movie,
//                            theaters = response.availableTheaters.map { createTheaterFromResponse(it) },
//                            selectedTheater = theater,
//                            availableDates = firstTheater.availableDates,
//                            seats = seats,
//                            isLoading = false
//                        )
//                    } else {
//                        uiState = uiState.copy(
//                            isLoading = false,
//                            errorMessage = "No schedules available"
//                        )
//                    }
//                },
//                onFailure = { error ->
//                    uiState = uiState.copy(
//                        isLoading = false,
//                        errorMessage = error.message ?: "Failed to load schedules"
//                    )
//                }
//            )
        }
    }

    fun selectTheater(theaterId: Int) {
        val theaterSchedule = movieScheduleResponse?.availableTheaters?.find {
            it.theaterId == theaterId
        } ?: return

        currentTheaterSchedule = theaterSchedule

        val theater = createTheaterFromResponse(theaterSchedule)
        val seats = generateSeatsForTheater(theater, emptyList())

        uiState = uiState.copy(
            selectedTheater = theater,
            availableDates = theaterSchedule.availableDates,
            seats = seats,
            selectedSeats = emptyList(),
            selectedDate = null,
            selectedShowTime = null,
            availableShowTimes = emptyList(),
            pricePerSeat = 0,
            totalPrice = 0
        )
    }

    fun selectDate(date: String) {
        val dateSchedule = uiState.availableDates.find { it.date == date } ?: return

        uiState = uiState.copy(
            selectedDate = date,
            availableShowTimes = dateSchedule.availableTimes,
            selectedShowTime = null,
            selectedSeats = emptyList(),
            pricePerSeat = 0,
            totalPrice = 0
        )

        // Reset seats to available when changing date
        val resetSeats = uiState.seats.mapValues { (_, columnMap) ->
            columnMap.mapValues { (_, seatList) ->
                seatList.map { seat -> seat.copy(status = SeatStatus.AVAILABLE) }
            }
        }
        uiState = uiState.copy(seats = resetSeats)
    }

    fun selectShowTime(timeSchedule: TimeSchedule) {
        val showTime = ShowTime(
            scheduleId = timeSchedule.scheduleId,
            time = timeSchedule.time,
            date = uiState.selectedDate ?: "",
            filledSeats = timeSchedule.filledSeats,
            price = timeSchedule.price
        )

        // Update seats with filled seats from this schedule
//        val updatedSeats = uiState.seats.mapValues { (seatId, seat) ->
//            when {
//                timeSchedule.filledSeats.contains(seatId) -> seat.copy(status = SeatStatus.UNAVAILABLE)
//                seat.status == SeatStatus.SELECTED -> seat.copy(status = SeatStatus.AVAILABLE)
//                else -> seat
//            }
//        }
        val updatedSeats = uiState.seats.mapValues { (_, columnMap) ->
            columnMap.mapValues { (_, seatList) ->
                seatList.map { seat ->
                    when {
                        timeSchedule.filledSeats.contains(seat.id) ->
                            seat.copy(status = SeatStatus.UNAVAILABLE)

                        seat.status == SeatStatus.SELECTED ->
                            seat.copy(status = SeatStatus.AVAILABLE)

                        else -> seat
                    }
                }
            }
        }

        uiState = uiState.copy(
            selectedShowTime = showTime,
            seats = updatedSeats,
            selectedSeats = emptyList(),
            pricePerSeat = timeSchedule.price,
            totalPrice = 0
        )
    }

//    fun toggleSeatSelection(seatId: String) {
//        val seat = uiState.seats[seatId] ?: return
//
//        if (uiState.selectedShowTime == null) {
//            uiState = uiState.copy(toastMessage = "Please select a showtime")
//            return
//        }
//        if (seat.status == SeatStatus.UNAVAILABLE) return
//        if (uiState.selectedSeats.size >= 8 && seat.status != SeatStatus.SELECTED){
//            uiState = uiState.copy(toastMessage = "You can only select up to 8 seats")
//            return
//        }
//
//        val newStatus = if (seat.status == SeatStatus.SELECTED) {
//            SeatStatus.AVAILABLE
//        } else {
//            SeatStatus.SELECTED
//        }
//
//        val updatedSeats = uiState.seats.toMutableMap()
//        updatedSeats[seatId] = seat.copy(status = newStatus)
//
//        val selectedSeats = updatedSeats.values.filter { it.status == SeatStatus.SELECTED }
//        val totalPrice  =selectedSeats.size * uiState.pricePerSeat
//
//        uiState = uiState.copy(
//            seats = updatedSeats,
//            selectedSeats = selectedSeats,
//            totalPrice = totalPrice
//        )
//    }

    fun toggleSeatSelection(seatId: String) {
        val found = findSeat(uiState.seats, seatId) ?: return
        val (sectionId, column, seat) = found

        if (uiState.selectedShowTime == null) {
            uiState = uiState.copy(toastMessage = "Please select a showtime")
            return
        }
        if (seat.status == SeatStatus.UNAVAILABLE) return
        if (uiState.selectedSeats.size >= 8 && seat.status != SeatStatus.SELECTED) {
            uiState = uiState.copy(toastMessage = "You can only select up to 8 seats")
            return
        }

        val newStatus = if (seat.status == SeatStatus.SELECTED)
            SeatStatus.AVAILABLE else SeatStatus.SELECTED

        val updatedSeats = uiState.seats.toMutableMap()
        val updatedColumn = updatedSeats[sectionId]!!.toMutableMap()
        val updatedList = updatedColumn[column]!!.map { s ->
            if (s.id == seatId) s.copy(status = newStatus) else s
        }

        updatedColumn[column] = updatedList
        updatedSeats[sectionId] = updatedColumn

        val selectedSeats = updatedSeats
            .flatMap { (_, colMap) -> colMap.values.flatten() }
            .filter { it.status == SeatStatus.SELECTED }

        val totalPrice = selectedSeats.size * uiState.pricePerSeat

        uiState = uiState.copy(
            seats = updatedSeats,
            selectedSeats = selectedSeats,
            totalPrice = totalPrice
        )
    }

    fun showConfirmationDialog() {
        paymentState = paymentState.copy(confirmPayment = true)
    }

    fun dismissConfirmationDialog() {
        paymentState = paymentState.copy(confirmPayment = false)
    }

    fun proceedToPayment() {
        // Data will be used for payment
        val paymentData = PaymentData(
            scheduleId = uiState.selectedShowTime!!.scheduleId,
            movieTitle = uiState.movie?.title ?: "",
            theaterName = uiState.selectedTheater?.name ?: "",
            date = uiState.selectedDate ?: "",
            time = uiState.selectedShowTime!!.time,
            selectedSeats = uiState.selectedSeats.map { it.id },
            totalPrice = uiState.totalPrice
        )

        viewModelScope.launch {
            paymentState = paymentState.copy(isLoading = true, confirmPayment = false)
            delay(2000)
            paymentState = paymentState.copy(
                isLoading = false,
                paymentSucceed = true
            )

            // Error handler
        }
    }

    private fun findSeat(seats: Map<String, Map<Int, List<Seat>>>, id: String): Triple<String, Int, Seat>? {
        seats.forEach { (sectionId, columns) ->
            columns.forEach { (col, list) ->
                list.forEach { seat ->
                    if (seat.id == id) return Triple(sectionId, col, seat)
                }
            }
        }
        return null
    }

    private fun createTheaterFromResponse(response: TheaterSchedule): Theater {
        val sections = generateSections(
            sectionCount = response.section,
            columnCount = response.column,
            rowCount = response.row
        )

        return Theater(
            id = response.theaterId,
            name = response.theaterName,
            sectionCount = response.section,
            rowCount = response.row,
            columnCount = response.column,
            sections = sections
        )
    }

    private fun generateSections(
        sectionCount: Int,
        columnCount: Int,
        rowCount: Int
    ): List<SeatSection> {
        val sections = mutableListOf<SeatSection>()
        val rowsPerSection = rowCount / sectionCount

        for (i in 0 until sectionCount) {
            val sectionName = ('A' + i).toString()
            sections.add(
                SeatSection(
                    id = sectionName,
                    name = sectionName,
                    rowStart = (i * rowsPerSection),
                    rowEnd = ((i + 1) * rowsPerSection) - 1,
                    columns = (1..columnCount).toList()
                )
            )
        }

        return sections
    }

    private fun generateSeatsForTheater(
        theater: Theater,
        filledSeats: List<String>
    ): Map<String, Map<Int, List<Seat>>> {
        val seats = LinkedHashMap<String, Seat>(theater.columnCount * theater.rowCount)

        for (colIndex in 0 until theater.columnCount) {
            val rowLabel = ('A' + colIndex).toString()

            for (row in 1..theater.rowCount) {
                val seatId = "$rowLabel$row"

                // Precomputed: langsung ambil section yang benar
                val section = theater.sections.first {
                    row - 1 in it.rowStart..it.rowEnd
                }

                seats[seatId] = Seat(
                    id = seatId,
                    row = rowLabel,
                    column = row,
                    sectionId = section.id,
                    displayLabel = seatId,
                    status = if (seatId in filledSeats) SeatStatus.UNAVAILABLE else SeatStatus.AVAILABLE
                )
            }
        }

        val seatSections = seats.values
            .groupBy { it.sectionId }
            .mapValues { (_, seatsInSection) ->
                seatsInSection
                    .groupBy { it.column }
                    .mapValues { (_, seatsInColumn) ->
                        seatsInColumn.sortedBy { it.row }
                    }
                    .toSortedMap()
            }

        return seatSections
    }

    fun clearPaymentError() {
        paymentState = paymentState.copy(errorMessage = null)
    }
}