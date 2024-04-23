package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.Response
import com.example.playlistmaker.search.data.model.SongDto
import com.google.gson.annotations.SerializedName

data class SongsSearchResponse(
    @SerializedName("resultCount")
    val resultCount: Int,
    @SerializedName("results")
    val songs: List<SongDto>
) : Response()
