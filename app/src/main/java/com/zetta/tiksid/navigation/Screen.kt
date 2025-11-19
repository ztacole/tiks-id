package com.zetta.tiksid.navigation

sealed class Screen(val route: String) {
    object SignIn: Screen("login")
    object SignUp: Screen("signup")
    object Home: Screen("home")
    object Browse: Screen("browse")
    object Booking: Screen("booking/{movieId}") {
        fun createRoute(movieId: Int) = "booking/$movieId"
    }
    object TicketList: Screen("ticket_list")
    object TicketDetail: Screen("ticket_detail/{ticketId}") {
        fun createRoute(ticketId: Int) = "ticket_detail/$ticketId"
    }
}