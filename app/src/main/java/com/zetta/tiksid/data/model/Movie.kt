package com.zetta.tiksid.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Movie(
    val id: Int,
    val title: String,
    val poster: String,
    val genre: String,
    val duration: Int
)
