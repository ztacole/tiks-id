package com.zetta.tiksid.data.model

import kotlinx.serialization.Serializable

@Serializable
data class TicketDetail(
    val id: Int,
    val movie: Movie,
    val schedule: String,
    val theater: String,
    val seats: List<String>
)
