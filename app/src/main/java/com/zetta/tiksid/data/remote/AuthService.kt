package com.zetta.tiksid.data.remote

import com.zetta.tiksid.data.model.LoginRequest
import com.zetta.tiksid.data.model.LoginResponse
import com.zetta.tiksid.data.model.common.MessageResponse
import com.zetta.tiksid.data.model.RefreshTokenRequest
import com.zetta.tiksid.data.model.RefreshTokenResponse
import com.zetta.tiksid.data.model.RegisterRequest
import com.zetta.tiksid.data.model.RegisterResponse
import com.zetta.tiksid.utils.safeApiCall
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class AuthService(private val client: HttpClient) {
    companion object {
        private const val BASE_ROUTE = "auth"

        private const val LOGIN = "$BASE_ROUTE/login"
        private const val REGISTER = "$BASE_ROUTE/register"
        private const val LOGOUT = "$BASE_ROUTE/logout"
        private const val REFRESH = "$BASE_ROUTE/refresh"
    }

    suspend fun login(email: String, password: String): Result<LoginResponse> = safeApiCall {
        client.post(LOGIN) {
            setBody(LoginRequest(email, password))
        }
    }

    suspend fun register(name: String, email: String, password: String): Result<RegisterResponse> = safeApiCall {
        client.post(REGISTER) {
            setBody(RegisterRequest(name, email, password))
        }
    }

    suspend fun logout(): Result<MessageResponse> = safeApiCall { client.post(LOGOUT) }

    suspend fun refreshToken(refreshToken: String): Result<RefreshTokenResponse> = safeApiCall {
        client.post(REFRESH) {
            setBody(RefreshTokenRequest(refreshToken))
        }
    }
}