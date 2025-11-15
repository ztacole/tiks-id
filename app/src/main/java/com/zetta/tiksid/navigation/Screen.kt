package com.zetta.tiksid.navigation

sealed class Screen(val route: String) {
    object SignIn: Screen("login")
    object SignUp: Screen("signup")
    object Home: Screen("home")
    object Browse: Screen("browse")
    object MovieDetail: Screen("movie_detail/{movieId}") {
        fun createRoute(movieId: String) = "movie_detail/$movieId"
    }
    object TicketList: Screen("ticket_list")
    object TicketDetail: Screen("ticket_detail/{ticketId}") {
        fun createRoute(ticketId: String) = "ticket_detail/$ticketId"
    }
}