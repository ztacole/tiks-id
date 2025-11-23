package com.zetta.tiksid.data.remote

import com.zetta.tiksid.data.model.Ticket
import com.zetta.tiksid.data.model.TicketRequest
import com.zetta.tiksid.data.model.common.BaseResponse
import com.zetta.tiksid.data.model.common.MessageResponse
import com.zetta.tiksid.utils.safeApiCall
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class TicketService(private val client: HttpClient) {
    companion object {
        private const val BASE_ROUTE = "tickets"

        private const val MY_BOOKINGS = "$BASE_ROUTE/my-bookings"
        private const val BOOK = "$BASE_ROUTE/book"
    }

    suspend fun getMyBookings(): Result<BaseResponse<List<Ticket>>> = safeApiCall {
        client.get(MY_BOOKINGS)
    }

    suspend fun getMyBookings(id: Int): Result<BaseResponse<Ticket>> = safeApiCall {
        client.get("$MY_BOOKINGS/$id")
    }

    suspend fun bookSeats(scheduleId: Int, seats: List<String>): Result<MessageResponse> = safeApiCall {
        client.post(BOOK) {
            setBody(TicketRequest(scheduleId, seats))
        }
    }
}