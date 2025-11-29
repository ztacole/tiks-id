package com.zetta.tiksid.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieOverview(
    val title: String,
    val description: String,
    val duration: Int,
    @SerialName("release_date") val releaseDate: String,
    val poster: String,
    val genres: List<String>,
    @SerialName("available_theaters") val availableTheaters: List<TheaterSchedule>?
)

@Serializable
data class TheaterSchedule(
    @SerialName("theater_id")
    val theaterId: Int,
    @SerialName("theater")
    val theaterName: String,
    val section: Int,
    val row: Int,
    val column: Int,
    @SerialName("available_dates")
    val availableDates: List<DateSchedule>
)

@Serializable
data class DateSchedule(
    @SerialName("date")
    val date: String, // "28 Nov 2025"
    @SerialName("available_times")
    val availableTimes: List<TimeSchedule>
)

@Serializable
data class TimeSchedule(
    @SerialName("schedule_id")
    val scheduleId: Int,
    val time: String,
    @SerialName("filled_seats")
    val filledSeats: List<String>,
    val price: Int
)
