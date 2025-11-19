package com.zetta.tiksid.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
