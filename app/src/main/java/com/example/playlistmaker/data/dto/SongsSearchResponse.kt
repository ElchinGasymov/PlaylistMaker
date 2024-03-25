package com.example.playlistmaker.data.dto

import com.google.gson.annotations.SerializedName

data class SongsSearchResponse(
    @SerializedName("resultCount")
    val resultCount: Int,
    @SerializedName("results")
    val songs: List<SongDto>
) : Response()
