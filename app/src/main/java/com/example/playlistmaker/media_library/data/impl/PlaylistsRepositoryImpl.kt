package com.example.playlistmaker.media_library.data.impl

import com.example.playlistmaker.media_library.data.db.AppDatabase
import com.example.playlistmaker.media_library.data.db.PlaylistDbConvertor
import com.example.playlistmaker.media_library.data.db.entity.PlaylistEntity
import com.example.playlistmaker.media_library.domain.db.PlaylistsRepository
import com.example.playlistmaker.new_playlist.domain.model.Playlist
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistsRepositoryImpl(
    private val database: AppDatabase,
    private val converter: PlaylistDbConvertor,
) : PlaylistsRepository {

    override suspend fun createPlaylist(playlist: Playlist) {
        database
            .playlistDao()
            .insertPlaylist(converter.map(playlist))
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        database
            .playlistDao()
            .deletePlaylist(converter.map(playlist))
    }

    override suspend fun updateTracks(playlist: Playlist) {
        database
            .playlistDao()
            .updatePlaylist(converter.map(playlist))
    }

    override fun getSavedPlaylists(): Flow<List<Playlist>> {
        return database
            .playlistDao()
            .getSavedPlaylists()
            .map { convertFromTrackEntity(it) }
    }

    private fun convertFromTrackEntity(playlists: List<PlaylistEntity>): List<Playlist> {
        return playlists.map { converter.map(it) }
    }
}
