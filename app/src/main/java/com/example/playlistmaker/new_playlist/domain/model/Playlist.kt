package com.example.playlistmaker.new_playlist.domain.model

import com.example.playlistmaker.search.domain.Track
import java.io.Serializable

data class Playlist(
    val id: Int,
    val coverImageUrl: String,
    val playlistName: String,
    val playlistDescription: String,
    var trackList: List<Track>,
    var tracksCount: Int,
) : Serializable {

    fun getShareText(): String {
        val shareText = StringBuilder()
        shareText.append(playlistName)
        shareText.append(" ")
        shareText.append(playlistDescription)
        shareText.append(" ")
        shareText.append(tracksCount)

        trackList.forEachIndexed { index, track ->
            shareText.append(index)
            shareText.append(". ")
            shareText.append(track.artistName)
            shareText.append(" - ")
            shareText.append(track.trackName)
            shareText.append(" ")
            shareText.append(track.trackTime)
        }
        return shareText.toString()
    }
}
