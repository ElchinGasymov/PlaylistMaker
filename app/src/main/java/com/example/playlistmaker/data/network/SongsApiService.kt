package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.dto.SongsSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SongsApiService {
    @GET("search")
    fun getSongs(
        @Query("term") text: String,
    ): Call<SongsSearchResponse>

}
