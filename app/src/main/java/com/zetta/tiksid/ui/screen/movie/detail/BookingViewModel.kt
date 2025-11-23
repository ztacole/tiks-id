package com.zetta.tiksid.ui.screen.movie.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zetta.tiksid.data.model.MovieBooking
import com.zetta.tiksid.data.model.TheaterSchedule
import com.zetta.tiksid.data.model.TimeSchedule
import com.zetta.tiksid.data.repository.MovieRepository
import com.zetta.tiksid.data.repository.TicketRepository
import com.zetta.tiksid.utils.formatDateToYear
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BookingViewModel(
    savedStateHandle: SavedStateHandle,
    private val movieRepository: MovieRepository,
    private val ticketRepository: TicketRepository
): ViewModel() {
    private val movieId: Int = savedStateHandle["movieId"] ?: 0

    var uiState by mutableStateOf(BookingUiState())
        private set
    var paymentState by mutableStateOf(PaymentState())
        private set

    private var movieScheduleResponse: MovieBooking? = null

    init {
        loadBookingData()
    }

    private fun loadBookingData() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)

            movieRepository.getMovieById(movieId).fold(
                onSuccess = { response ->
                    movieScheduleResponse = response

                    // Use background thread
                    val movieDetail = withContext(Dispatchers.Default) {
                        MovieDetail(
                            id = movieId,
                            title = response.title,
                            description = response.description,
                            duration = response.duration,
                            releaseDate = formatDateToYear(response.releaseDate),
                            poster = response.poster,
                            genres = response.genres
                        )
                    }

                    if (response.availableTheaters.isNullOrEmpty()) {
                        uiState = uiState.copy(
                            movie = movieDetail,
                            isLoading = false,
                        )
                    } else {
                        val firstTheater = response.availableTheaters.first()

                        // Generate seats on background thread
                        val (theater, seats) = withContext(Dispatchers.Default) {
                            val theaterObj = createTheaterFromResponse(firstTheater)
                            val seatsObj = generateSeatsForTheater(theaterObj, emptyList())
                            theaterObj to seatsObj
                        }

                        // Single state update
                        uiState = uiState.copy(
                            movie = movieDetail,
                            theaters = response.availableTheaters.map {
                                withContext(Dispatchers.Default) {
                                    createTheaterFromResponse(it)
                                }
                            },
                            selectedTheater = theater,
                            availableDates = firstTheater.availableDates,
                            seats = seats,
                            isLoading = false
                        )
                    }
                },
                onFailure = { error ->
                    uiState = uiState.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "Failed to load schedules"
                    )
                }
            )
        }
    }

    fun selectTheater(theaterId: Int) {
        if (theaterId == uiState.selectedTheater?.id) return

        val theaterSchedule = movieScheduleResponse?.availableTheaters?.find {
            it.theaterId == theaterId
        } ?: return

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
        if (date == uiState.selectedDate) return

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
        if (timeSchedule.time == uiState.selectedShowTime?.time) return

        val showTime = ShowTime(
            scheduleId = timeSchedule.scheduleId,
            time = timeSchedule.time,
            date = uiState.selectedDate ?: "",
            filledSeats = timeSchedule.filledSeats,
            price = timeSchedule.price
        )

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
        viewModelScope.launch {
            paymentState = paymentState.copy(isLoading = true, confirmPayment = false)

            ticketRepository.bookSeats(
                uiState.selectedShowTime!!.scheduleId,
                uiState.selectedSeats.map { it.id }
            ).onSuccess {
                paymentState = paymentState.copy(
                    isLoading = false,
                    paymentSucceed = true
                )
            }.onFailure {
                paymentState = paymentState.copy(
                    isLoading = false,
                    errorMessage = it.message ?: "Failed to book seats"
                )
            }
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
        val filledSet = filledSeats.toSet() // O(n) lookup instead of O(n²)

        val seatSections = LinkedHashMap<String, MutableMap<Int, MutableList<Seat>>>()

        // Initialize sections
        theater.sections.forEach { section ->
            seatSections[section.id] = LinkedHashMap()
        }

        for (colIndex in 0 until theater.columnCount) {
            val rowLabel = ('A' + colIndex).toString()

            for (row in 1..theater.rowCount) {
                val seatId = "$rowLabel$row"

                val section = theater.sections.firstOrNull {
                    (row - 1) in it.rowStart..it.rowEnd
                } ?: continue

                val seat = Seat(
                    id = seatId,
                    row = rowLabel,
                    column = row,
                    sectionId = section.id,
                    displayLabel = seatId,
                    status = if (seatId in filledSet) SeatStatus.UNAVAILABLE else SeatStatus.AVAILABLE
                )

                seatSections[section.id]!!
                    .getOrPut(row) { mutableListOf() }
                    .add(seat)
            }
        }

        return seatSections.mapValues { (_, columns) ->
            columns.mapValues { (_, seats) -> seats.sortedBy { it.row } }
        }
    }

    fun clearPaymentError() {
        paymentState = paymentState.copy(errorMessage = null)
    }
}