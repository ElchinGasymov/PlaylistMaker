package com.example.playlistmaker.search.data.model


import com.google.gson.annotations.SerializedName

data class SongDto(
    @SerializedName("artistName")
    val artistName: String,
    @SerializedName("artworkUrl100")
    val artworkUrl100: String,
    @SerializedName("trackName")
    val trackName: String?,
    @SerializedName("trackTimeMillis")
    val trackTimeMillis: Long,
    @SerializedName("trackId")
    val trackId: Int,
    @SerializedName("collectionName")
    val collectionName: String?,
    @SerializedName("releaseDate")
    val releaseDate: String?,
    @SerializedName("primaryGenreName")
    val primaryGenreName: String?,
    @SerializedName("country")
    val country: String?,
    @SerializedName("previewUrl")
    val previewUrl: String?
)
