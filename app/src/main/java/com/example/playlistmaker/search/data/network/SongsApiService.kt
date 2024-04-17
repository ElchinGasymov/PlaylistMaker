package com.example.playlistmaker.search.data.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SongsApiService {
    @GET("search")
    fun getSongs(
        @Query("term") text: String,
    ): Call<SongsSearchResponse>
}