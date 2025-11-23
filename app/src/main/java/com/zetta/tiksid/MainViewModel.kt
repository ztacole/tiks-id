package com.zetta.tiksid

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zetta.tiksid.data.repository.AuthRepository
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: AuthRepository
): ViewModel() {

    fun refreshToken(refreshToken: String) {
        viewModelScope.launch { repository.refreshToken(refreshToken) }
    }
}