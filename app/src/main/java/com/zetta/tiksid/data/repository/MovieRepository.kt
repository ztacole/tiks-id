package com.zetta.tiksid.data.repository

import com.zetta.tiksid.data.model.Movie
import com.zetta.tiksid.data.model.MovieOverview
import com.zetta.tiksid.data.model.common.MetaResponse
import com.zetta.tiksid.data.remote.MovieService

class MovieRepository(private val api: MovieService) {
    suspend fun getMovies(page: Int, limit: Int): Result<MetaResponse<List<Movie>>> {
        val response = api.getMovies(page, limit)
        return if (response.isSuccess) {
            response.getOrNull()?.let {
                Result.success(it)
            } ?: Result.failure(Exception("Empty response"))
        } else {
            Result.failure(response.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }

    suspend fun getMovieById(id: Int): Result<MovieOverview> {
        val response = api.getMovieById(id)
        return if (response.isSuccess) {
            response.getOrNull()?.let {
                Result.success(it.data)
            } ?: Result.failure(Exception("Empty response"))
        } else {
            Result.failure(response.exceptionOrNull() ?: Exception("Unknown error"))
        }
    }
}