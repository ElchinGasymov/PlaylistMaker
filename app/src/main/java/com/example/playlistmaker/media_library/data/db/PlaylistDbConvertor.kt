package com.example.playlistmaker.media_library.data.db

import com.example.playlistmaker.media_library.data.db.entity.PlaylistEntity
import com.example.playlistmaker.new_playlist.domain.model.Playlist
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class PlaylistDbConvertor {
    fun map(playlist: PlaylistEntity): Playlist {
        return Playlist(
            id = playlist.playlistId,
            coverImageUrl = playlist.coverImageUrl,
            playlistName = playlist.playlistName,
            playlistDescription = playlist.playlistDescription,
            trackList = if (playlist.trackList.isNotEmpty()) {
                Json.decodeFromString(playlist.trackList)
            } else listOf(),
            tracksCount = playlist.countTracks
        )
    }

    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            playlistId = playlist.id,
            playlistName = playlist.playlistName,
            playlistDescription = playlist.playlistDescription,
            coverImageUrl = playlist.coverImageUrl,
            trackList = if (playlist.trackList.isNotEmpty()) {
                Json.encodeToString(playlist.trackList)
            } else "",
            countTracks = playlist.tracksCount,
            saveDate = System.currentTimeMillis()
        )
    }
}