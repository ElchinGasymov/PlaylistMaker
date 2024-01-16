package com.example.playlistmaker.network

import com.google.gson.annotations.SerializedName

data class SongsResponse(
    @SerializedName("resultCount")
    val resultCount: Int,
    @SerializedName("results")
    val songs: List<Song>
)
