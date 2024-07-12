package com.example.playlistmaker.media_library.domain.db

import com.example.playlistmaker.new_playlist.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {
    suspend fun createPlaylist(playlist: Playlist)

    suspend fun deletePlaylist(playlist: Playlist)

    suspend fun updateTracks(playlist: Playlist)

    fun getSavedPlaylists(): Flow<List<Playlist>>
}