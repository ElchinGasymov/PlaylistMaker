package com.example.playlistmaker.new_playlist.domain.model

import com.example.playlistmaker.search.domain.Track
import java.io.Serializable

data class Playlist(
    val id: Int,
    val coverImageUrl: String,
    val playlistName: String,
    val playlistDescription:String,
    var trackList: List<Track>,
    var tracksCount: Int,
) : Serializable
