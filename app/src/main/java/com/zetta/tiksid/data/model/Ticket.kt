package com.zetta.tiksid.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Ticket(
    val id: Int,
    @SerialName("movie_title")
    val movieTitle: String,
    @SerialName("movie_duration")
    val movieDuration: Int,
    @SerialName("movie_poster")
    val moviePoster: String,
    @SerialName("movie_genre")
    val movieGenres: List<String>,
    @SerialName("theater_name")
    val theaterName: String,
    @SerialName("schedule_date")
    val scheduleDate: String,
    val seats: List<String>,
    @SerialName("total_price") val totalPrice: Int
)

@Serializable
data class TicketRequest(
    @SerialName("schedule_id")
    val scheduleId: Int,
    val seats: List<String>
)