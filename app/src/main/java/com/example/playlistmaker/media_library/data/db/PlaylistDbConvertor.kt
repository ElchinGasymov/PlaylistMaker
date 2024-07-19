package com.example.playlistmaker.media_library.data.db

import com.example.playlistmaker.media_library.data.db.entity.PlaylistEntity
import com.example.playlistmaker.media_library.data.db.entity.TrackInPlaylistEntity
import com.example.playlistmaker.new_playlist.domain.model.Playlist
import com.google.gson.Gson

class PlaylistDbConvertor {
    fun map(playlist: PlaylistEntity, tracks: List<TrackInPlaylistEntity>): Playlist {
        return Playlist(
            id = playlist.playlistId,
            coverImageUrl = playlist.coverImageUrl,
            playlistName = playlist.playlistName,
            playlistDescription = playlist.playlistDescription,
            trackList = tracks.map { it.toDomain() },
            tracksCount = playlist.countTracks
        )
    }

    fun map(playlist: Playlist): PlaylistEntity {
        val gson = Gson()
        val tracks = mutableListOf<String>()
        playlist.trackList.forEach {
            tracks.add(gson.toJson(it.id))
        }

        return PlaylistEntity(
            playlistId = playlist.id,
            playlistName = playlist.playlistName,
            playlistDescription = playlist.playlistDescription,
            coverImageUrl = playlist.coverImageUrl,
            trackList = if (playlist.trackList.isNotEmpty()) {
                gson.toJson(tracks)
            } else "",
            countTracks = playlist.tracksCount,
            saveDate = System.currentTimeMillis()
        )
    }
}