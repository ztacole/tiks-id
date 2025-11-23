package com.zetta.tiksid.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val error: String
)
