package com.zetta.tiksid.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Theater(
    val id: Int,
    val name: String,
    val section: Int,
    val column: Int,
    val row: Int
)
