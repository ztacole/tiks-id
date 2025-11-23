package com.zetta.tiksid.data.remote

import com.zetta.tiksid.data.model.Movie
import com.zetta.tiksid.data.model.MovieBooking
import com.zetta.tiksid.data.model.common.BaseResponse
import com.zetta.tiksid.data.model.common.MetaResponse
import com.zetta.tiksid.utils.safeApiCall
import io.ktor.client.HttpClient
import io.ktor.client.request.get

class MovieService(private val client: HttpClient) {
    companion object {
        const val BASE_ROUTE = "movie"
    }

    suspend fun getMovies(page: Int, limit: Int): Result<MetaResponse<List<Movie>>> = safeApiCall {
        client.get("$BASE_ROUTE?page=$page&per_page=$limit")
    }

    suspend fun getMovieById(id: Int): Result<BaseResponse<MovieBooking>> = safeApiCall {
        client.get("$BASE_ROUTE/$id")
    }
}