package com.zetta.tiksid.data.model

data class ScheduleTime(
    val id: Int,
    val time: String,
    val bookedSeats: List<String>
)
