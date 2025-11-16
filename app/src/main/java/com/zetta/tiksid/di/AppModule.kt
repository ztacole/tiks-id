package com.zetta.tiksid.di

import androidx.lifecycle.SavedStateHandle
import com.zetta.tiksid.network.ApiClient
import com.zetta.tiksid.network.SessionManager
import com.zetta.tiksid.ui.screen.auth.signin.SignInViewModel
import com.zetta.tiksid.ui.screen.auth.signup.SignUpViewModel
import com.zetta.tiksid.ui.screen.movie.browse.BrowseViewModel
import com.zetta.tiksid.ui.screen.movie.detail.MovieDetailViewModel
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

    viewModel { SignInViewModel(get()) }
    viewModel { SignUpViewModel(get()) }
    viewModel { HomeViewModel() }
    viewModel { BrowseViewModel() }
    viewModel { MovieDetailViewModel() }
    viewModel { TicketListViewModel() }
    viewModel { (handle: SavedStateHandle) ->
        TicketDetailViewModel(handle)
    }
}