package com.zetta.tiksid.data.model.common

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val error: String
)
