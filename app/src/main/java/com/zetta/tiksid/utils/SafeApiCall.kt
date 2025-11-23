package com.zetta.tiksid.utils

import android.accounts.NetworkErrorException
import android.util.Log
import com.zetta.tiksid.data.model.common.ErrorResponse
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess

suspend inline fun <reified T> safeApiCall(
    crossinline apiCall: suspend () -> HttpResponse
): Result<T> {
    return try {
        val response = apiCall()

        if (response.status.isSuccess()) {
            val data = response.body<T>()
            Result.success(data)
        } else {
            val errorBody = try {
                response.body<ErrorResponse>().error
            } catch (_: Exception) {
                "Server error: ${response.status}"
            }
            Result.failure(Exception(errorBody))
        }
    } catch (e: Exception) {
        Result.failure(
            when (e) {
                is HttpRequestTimeoutException -> Exception("Request timed out")
                is NetworkErrorException -> Exception("No internet connection")
                else -> Exception("Unexpected error: ${e.message}")
            }
        )
    }
}
