package com.example.playlistmaker.media_library.domain.db

import com.example.playlistmaker.new_playlist.domain.model.Playlist
import com.example.playlistmaker.search.domain.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {
    suspend fun createPlaylist(playlist: Playlist)

    suspend fun deletePlaylist(playlist: Playlist)

    suspend fun updateTracks(track: Track, playlist: Playlist)

    fun getSavedPlaylists(): Flow<List<Playlist>>
}