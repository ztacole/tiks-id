package com.zetta.tiksid.data.model

data class ScheduleDate(
    val id: Int,
    val theaterId: Int,
    val date: String,
    val times: List<ScheduleTime>
)
