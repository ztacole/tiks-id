package com.zetta.tiksid.network

import kotlinx.serialization.Serializable

@Serializable
data class BaseMessageResponse(
    val success: Boolean,
    val message: String
)
