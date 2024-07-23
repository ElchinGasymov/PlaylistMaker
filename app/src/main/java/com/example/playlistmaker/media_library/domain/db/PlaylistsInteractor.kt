package com.example.playlistmaker.media_library.domain.db

import com.example.playlistmaker.new_playlist.domain.model.Playlist
import com.example.playlistmaker.search.domain.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistsInteractor {
    fun getPlaylists(): Flow<List<Playlist>>
    fun isTrackAlreadyExists(playlist: Playlist, track: Track): Boolean
    suspend fun addTrackToPlaylist(playlist: Playlist, track: Track)
    suspend fun deleteTrack(trackId: Int, playlist: Playlist)
    suspend fun deletePlaylist(playlist: Playlist)
    suspend fun getPlaylistById(id: Int): Flow<Playlist>
}