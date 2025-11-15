package com.zetta.tiksid.network

import com.zetta.tiksid.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.plugin
import io.ktor.client.request.header
import io.ktor.client.statement.request
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.fullPath
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import java.io.IOException

class ApiClient(
    private val sessionManager: SessionManager
) {
    val client = HttpClient(CIO) {
        // Configure HTTP Client
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
                encodeDefaults = true
            })
        }
        install(DefaultRequest) {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
        }
        defaultRequest {
            url(BuildConfig.BASE_URL)
        }

        // Configure Retry Policy
        install(HttpRequestRetry) {
            retryOnServerErrors(maxRetries = 3)
            retryOnExceptionIf(maxRetries = 3) { req, cause ->
                cause is IOException
            }
            exponentialDelay()
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 10000
        }

        // Configure Cache Plugin
        install(HttpCache)

        // Configure Logging Plugin
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.BODY
            sanitizeHeader { header -> header == HttpHeaders.Authorization }
        }

        HttpResponseValidator {
            validateResponse {
                if (it.status == HttpStatusCode.Unauthorized && !it.request.url.fullPath.contains("/auth/login")) {
                    CoroutineScope(Dispatchers.IO).launch { sessionManager.clearSession() }
                }
            }
        }
    }.also { client ->
        client.plugin(HttpSend).intercept { request ->
            val token = sessionManager.getToken()
            if (!token.isNullOrEmpty()) {
                request.headers.append(HttpHeaders.Authorization, "Bearer $token")
            }
            execute(request)
        }
    }
}