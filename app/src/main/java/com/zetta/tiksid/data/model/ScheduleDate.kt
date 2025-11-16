package com.zetta.tiksid.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ScheduleDate(
    val id: Int,
    @SerialName("theater_id") val theaterId: Int,
    val date: String,
    val times: List<ScheduleTime>
)
