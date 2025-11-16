package com.zetta.tiksid.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class History(
    val id: Int,
    val movie: Movie,
    val schedule: String,
    val seats: List<String>,
    @SerialName("total_price") val totalPrice: Int
)
