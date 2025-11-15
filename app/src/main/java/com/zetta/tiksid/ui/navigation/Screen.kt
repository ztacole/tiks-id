package com.zetta.tiksid.ui.navigation

sealed class Screen(val route: String) {
    object SignIn: Screen("login")
    object SignUp: Screen("signup")
    object Home: Screen("home")
    object Browse: Screen("browse")
    object MovieDetail: Screen("movie_detail") {
        fun createRoute(movieId: String) = "movie_detail/$movieId"
    }
    object TicketList: Screen("ticket_list")
    object TicketDetail: Screen("ticket_detail") {
        fun createRoute(ticketId: String) = "ticket_detail/$ticketId"
    }
}