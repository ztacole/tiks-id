package com.zetta.tiksid.di

import com.zetta.tiksid.network.ApiClient
import com.zetta.tiksid.network.SessionManager
import com.zetta.tiksid.ui.screen.auth.AuthViewModel
import com.zetta.tiksid.ui.screen.movie.MovieViewModel
import com.zetta.tiksid.utils.ResourceProvider
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { ResourceProvider(get()) }

    single { SessionManager(get()) }
    single { ApiClient(get()) }

    viewModel { AuthViewModel(get()) }
    viewModel { MovieViewModel() }
}