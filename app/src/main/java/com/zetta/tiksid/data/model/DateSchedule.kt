package com.zetta.tiksid.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DateSchedule(
    @SerialName("date")
    val date: String, // "28 Nov 2025"
    @SerialName("available_times")
    val availableTimes: List<TimeSchedule>
)
