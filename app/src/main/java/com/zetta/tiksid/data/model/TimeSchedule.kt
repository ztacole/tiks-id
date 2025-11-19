package com.zetta.tiksid.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TimeSchedule(
    @SerialName("schedule_id")
    val scheduleId: Int,
    val time: String,
    @SerialName("filled_seats")
    val filledSeats: List<String>,
    val price: Int
)
