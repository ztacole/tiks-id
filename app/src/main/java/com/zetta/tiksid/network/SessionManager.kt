package com.zetta.tiksid.network

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.zetta.tiksid.data.model.LoginResponse
import com.zetta.tiksid.data.model.RefreshTokenResponse
import com.zetta.tiksid.data.model.User
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

val Context.tokenDataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_prefs")

class SessionManager(
    private val context: Context
) {
    companion object {
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
        private val TOKEN_TYPE_KEY = stringPreferencesKey("token_type")
        private val USER_SESSION_KEY = stringPreferencesKey("user_session")
    }

    private val _isLoggedOut = MutableSharedFlow<Boolean>()
    val isLoggedOut: SharedFlow<Boolean> = _isLoggedOut.asSharedFlow()

    private val _isSessionRefreshed = MutableSharedFlow<Boolean>()
    val isSessionRefreshed: SharedFlow<Boolean> = _isSessionRefreshed.asSharedFlow()

    private val accessTokenFlow = context.tokenDataStore.data.map { prefs -> prefs[ACCESS_TOKEN_KEY] }
    private val refreshTokenFlow = context.tokenDataStore.data.map { prefs -> prefs[REFRESH_TOKEN_KEY] }
    private val tokenTypeFlow = context.tokenDataStore.data.map { prefs -> prefs[TOKEN_TYPE_KEY] }

    suspend fun saveSession(session: LoginResponse) {
        context.tokenDataStore.edit { prefs ->
            prefs[USER_SESSION_KEY] = Json.encodeToString(session.user)
            prefs[ACCESS_TOKEN_KEY] = session.accessToken
            prefs[REFRESH_TOKEN_KEY] = session.refreshToken
            prefs[TOKEN_TYPE_KEY] = session.tokenType
        }
    }

    suspend fun clearSession() {
        context.tokenDataStore.edit { it.clear() }
        _isLoggedOut.emit(true)
    }

    suspend fun triggerRefreshSession() = _isSessionRefreshed.emit(true)

    suspend fun refreshSession(session: RefreshTokenResponse) {
        context.tokenDataStore.edit { prefs ->
            prefs[ACCESS_TOKEN_KEY] = session.accessToken
            prefs[REFRESH_TOKEN_KEY] = session.refreshToken
            prefs[TOKEN_TYPE_KEY] = session.tokenType
        }
        _isSessionRefreshed.emit(false)
    }

    suspend fun getSession(): User? {
        val json = context.tokenDataStore.data.map { prefs ->
            prefs[USER_SESSION_KEY]
        }.first()
        return json?.let { Json.decodeFromString<User>(it) }
    }

    suspend fun getAccessToken(): String? = accessTokenFlow.firstOrNull()

    suspend fun getRefreshToken(): String? = refreshTokenFlow.firstOrNull()

    suspend fun getTokenType(): String? = tokenTypeFlow.firstOrNull()
}
