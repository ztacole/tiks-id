package com.zetta.tiksid.data.repository

import com.zetta.tiksid.data.remote.AuthService
import com.zetta.tiksid.network.SessionManager

class AuthRepository(
    private val api: AuthService,
    private val sessionManager: SessionManager
) {
    suspend fun login(email: String, password: String): Result<Unit> {
        val response = api.login(email, password)
        return if (response.isSuccess) {
            response.getOrNull()?.let {
                sessionManager.saveSession(it)
                Result.success(Unit)
            } ?: Result.failure(Exception("Login failed"))
        } else {
            Result.failure(response.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }

    suspend fun register(name: String, email: String, password: String): Result<Unit> {
        val response = api.register(name, email, password)
        return if (response.isSuccess) {
            response.getOrNull()?.let {
                Result.success(Unit)
            } ?: Result.failure(Exception("Registration failed"))
        } else {
            Result.failure(response.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }

    suspend fun logout(): Result<Unit> {
        val response = api.logout()
        if (response.isSuccess) {
            sessionManager.clearSession()
            return Result.success(Unit)
        } else {
            return Result.failure(response.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }

    suspend fun refreshToken(refreshToken: String): Result<Unit> {
        val response = api.refreshToken(refreshToken)
        return if (response.isSuccess) {
            response.getOrNull()?.let {
                sessionManager.refreshSession(it)
                Result.success(Unit)
            } ?: Result.failure(Exception("Token refresh failed"))
        } else {
            sessionManager.clearSession()
            Result.failure(response.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }
}