package com.example.playlistmaker.search.data.network

import retrofit2.http.GET
import retrofit2.http.Query

interface SongsApiService {
    @GET("search")
    suspend fun getSongs(
        @Query("term") text: String,
    ): SongsSearchResponse
}
