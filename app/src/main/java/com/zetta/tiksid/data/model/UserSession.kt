package com.zetta.tiksid.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Role(
    val id: Int,
    val name: String
)

@Serializable
data class User(
    val id: Int,
    val email: String,
    @SerialName("full_name") val fullName: String,
    val role: Role
)

@Serializable
data class UserSession(
    val user: User,
    val token: String
)