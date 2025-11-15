package com.zetta.tiksid.network

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.zetta.tiksid.data.model.UserSession
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
        private val USER_SESSION_KEY = stringPreferencesKey("user_session")
    }

    private val _isLoggedOut = MutableSharedFlow<Boolean>()
    val isLoggedOut: SharedFlow<Boolean> = _isLoggedOut.asSharedFlow()

    suspend fun saveSession(session: UserSession) {
        context.tokenDataStore.edit { prefs ->
            prefs[USER_SESSION_KEY] = Json.encodeToString(session)
        }
    }

    suspend fun getSession(): UserSession? {
        val json = context.tokenDataStore.data.map { prefs ->
            prefs[USER_SESSION_KEY]
        }.first()
        return json?.let { Json.decodeFromString<UserSession>(it) }
    }

    suspend fun clearSession() {
        context.tokenDataStore.edit { it.clear() }
        _isLoggedOut.emit(true)
    }

    val tokenFlow = context.tokenDataStore.data.map { prefs ->
        prefs[USER_SESSION_KEY]?.let {
            Json.decodeFromString<UserSession>(it).token
        }
    }

    suspend fun getToken(): String? {
        return tokenFlow.firstOrNull()
    }
}
