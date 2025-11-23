package com.zetta.tiksid.di

import androidx.lifecycle.SavedStateHandle
import com.zetta.tiksid.MainViewModel
import com.zetta.tiksid.data.remote.AuthService
import com.zetta.tiksid.data.remote.MovieService
import com.zetta.tiksid.data.repository.AuthRepository
import com.zetta.tiksid.data.repository.MovieRepository
import com.zetta.tiksid.network.ApiClient
import com.zetta.tiksid.network.SessionManager
import com.zetta.tiksid.ui.screen.auth.signin.SignInViewModel
import com.zetta.tiksid.ui.screen.auth.signup.SignUpViewModel
import com.zetta.tiksid.ui.screen.movie.browse.BrowseViewModel
import com.zetta.tiksid.ui.screen.movie.detail.BookingViewModel
import com.zetta.tiksid.ui.screen.movie.home.HomeViewModel
import com.zetta.tiksid.ui.screen.ticket.detail.TicketDetailViewModel
import com.zetta.tiksid.ui.screen.ticket.list.TicketListViewModel
import com.zetta.tiksid.utils.ResourceProvider
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { ResourceProvider(get()) }

    single { SessionManager(get()) }
    single { ApiClient(get()) }

    single { AuthService(get<ApiClient>().client) }
    single { MovieService(get<ApiClient>().client) }

    single { AuthRepository(get(), get()) }
    single { MovieRepository(get()) }

    viewModel { MainViewModel(get()) }
    viewModel { SignInViewModel(get(), get()) }
    viewModel { SignUpViewModel(get(), get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { BrowseViewModel(get()) }
    viewModel {(handle: SavedStateHandle) ->
        BookingViewModel(handle, get())
    }
    viewModel { TicketListViewModel() }
    viewModel { (handle: SavedStateHandle) ->
        TicketDetailViewModel(handle)
    }
}