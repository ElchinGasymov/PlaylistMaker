package com.example.playlistmaker.media_library.data.db

import com.example.playlistmaker.media_library.data.db.entity.PlaylistEntity
import com.example.playlistmaker.new_playlist.domain.model.Playlist
import com.example.playlistmaker.search.domain.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PlaylistDbConvertor {
    fun map(playlist: PlaylistEntity): Playlist {
        val gson = Gson()
        val listOfJsons = gson.fromJson<List<String>>(playlist.trackList, object :
            TypeToken<List<String>>() {}.type)

        val tracks: List<Track> = if (!listOfJsons.isNullOrEmpty()) {
            listOfJsons.map {
                gson.fromJson(
                    it,
                    Track::class.java
                )
            }
        } else {
            emptyList()
        }

        return Playlist(
            id = playlist.playlistId,
            coverImageUrl = playlist.coverImageUrl,
            playlistName = playlist.playlistName,
            playlistDescription = playlist.playlistDescription,
            trackList = if (playlist.trackList.isNotEmpty()) {
                tracks
            } else listOf(),
            tracksCount = playlist.countTracks
        )
    }

    fun map(playlist: Playlist): PlaylistEntity {
        val gson = Gson()
        val tracks = mutableListOf<String>()
        playlist.trackList.forEach {
            tracks.add(gson.toJson(it))
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