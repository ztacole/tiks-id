package com.zetta.tiksid.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieDetail(
    val id: Int,
    val title: String,
    val description: String,
    val duration: Int,
    @SerialName("release_date") val releaseDate: String,
    val poster: String,
    val genres: List<String>
)
