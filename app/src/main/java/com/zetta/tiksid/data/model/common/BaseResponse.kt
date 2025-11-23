package com.zetta.tiksid.data.model.common

import kotlinx.serialization.Serializable

@Serializable
data class BaseResponse<out T>(
    val data: T,
    val message: String
)
