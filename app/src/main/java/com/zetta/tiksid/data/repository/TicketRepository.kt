package com.zetta.tiksid.data.repository

import com.zetta.tiksid.data.model.Ticket
import com.zetta.tiksid.data.remote.TicketService

class TicketRepository(private val api: TicketService) {
    suspend fun getMyBookings(): Result<List<Ticket>> {
        val response = api.getMyBookings()
        return if (response.isSuccess) {
            response.getOrNull()?.let {
                Result.success(it.data)
            } ?: Result.failure(Exception("Failed to fetch my bookings"))
        } else {
            Result.failure(response.exceptionOrNull() ?: Exception("Failed to fetch my bookings"))
        }
    }

    suspend fun getMyBookings(id: Int): Result<Ticket> {
        val response = api.getMyBookings(id)
        return if (response.isSuccess) {
            response.getOrNull()?.let {
                Result.success(it.data)
            } ?: Result.failure(Exception("Failed to fetch my bookings"))
        } else {
            Result.failure(response.exceptionOrNull() ?: Exception("Failed to fetch my bookings"))
        }
    }

    suspend fun bookSeats(scheduleId: Int, seats: List<String>): Result<Unit> {
        val response = api.bookSeats(scheduleId, seats)
        return if (response.isSuccess) {
            response.getOrNull()?.let {
                Result.success(Unit)
            } ?: Result.failure(Exception("Failed to book seats"))
        } else {
            Result.failure(response.exceptionOrNull() ?: Exception("Failed to book seats"))
        }
    }
}