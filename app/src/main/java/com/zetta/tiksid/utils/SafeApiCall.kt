package com.zetta.tiksid.utils

import com.zetta.tiksid.network.BaseMessageResponse
import io.ktor.client.call.body
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
                response.body<BaseMessageResponse>().message
            } catch (_: Exception) {
                "Server error: ${response.status}"
            }
            Result.failure(Exception(errorBody))
        }
    } catch (e: Exception) {
        // Handle exceptions like network errors or parsing errors
        Result.failure(e)
    }
}
