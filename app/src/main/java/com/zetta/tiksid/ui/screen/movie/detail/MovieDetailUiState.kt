package com.zetta.tiksid.ui.screen.movie.detail

import com.zetta.tiksid.data.model.MovieDetail
import com.zetta.tiksid.data.model.ScheduleDate
import com.zetta.tiksid.data.model.ScheduleTime
import com.zetta.tiksid.data.model.Theater

data class MovieDetailUiState(
    val isLoading: Boolean = false,
    val movie: MovieDetail? = null,
    val theaters: List<Theater> = emptyList(),
    val selectedTheater: Theater? = null,
    val schedules: List<ScheduleDate> = emptyList(),
    val selectedScheduleDate: ScheduleDate? = null,
    val selectedScheduleTime: ScheduleTime? = null,
    val seats: List<String> = emptyList(),
    val selectedSeats: List<String> = emptyList(),
    val errorMessage: String? = null
)
