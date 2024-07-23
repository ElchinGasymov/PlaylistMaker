package com.example.playlistmaker.media_library.data.impl

import com.example.playlistmaker.media_library.data.db.AppDatabase
import com.example.playlistmaker.media_library.data.db.PlaylistDbConvertor
import com.example.playlistmaker.media_library.data.db.entity.PlaylistEntity
import com.example.playlistmaker.media_library.data.db.entity.TrackInPlaylistEntity
import com.example.playlistmaker.media_library.domain.db.PlaylistsRepository
import com.example.playlistmaker.new_playlist.domain.model.Playlist
import com.example.playlistmaker.search.domain.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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

    override suspend fun updateTracks(track: Track, playlist: Playlist) {
        database
            .trackDao()
            .addTrack(TrackInPlaylistEntity.fromDomain(track))

        updatePlaylist(playlist)
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        database
            .playlistDao()
            .updatePlaylist(converter.map(playlist))
    }

    override suspend fun getPlaylistById(id: Int): Flow<Playlist> {
        return database
            .playlistDao()
            .getPlaylistById(id)
            .map { mapPlaylistToDomain(it) }
    }

    override suspend fun deleteTrackIfItNowhereElse(trackId: Int) {
        val playlists = convertFromTrackEntity(
            database
                .playlistDao()
                .getAllPlaylists()
        )

        var isNeedToDelete = true
        playlists.forEach { playlist ->
            if (playlist.trackList.any { it.id == trackId }) {
                isNeedToDelete = false
            }
        }
        if (isNeedToDelete) {
            database
                .trackDao()
                .getTrack(trackId)
        }
    }

    override fun getSavedPlaylists(): Flow<List<Playlist>> {
        return database
            .playlistDao()
            .getSavedPlaylists()
            .map { convertFromTrackEntity(it) }
    }

    private suspend fun convertFromTrackEntity(playlists: List<PlaylistEntity>): List<Playlist> {
        return playlists.map { playlistEntity ->
            mapPlaylistToDomain(playlistEntity)
        }
    }

    private suspend fun mapPlaylistToDomain(playlistEntity: PlaylistEntity?): Playlist {
        val gson = Gson()
        val tracksEntity = mutableListOf<TrackInPlaylistEntity>()
        val tracksId = if (playlistEntity?.trackList?.isNotEmpty() == true) {
            gson.fromJson<List<String>>(
                playlistEntity.trackList,
                object : TypeToken<List<String>>() {}.type
            )
        } else listOf()

        tracksId?.forEach { trackId ->
            val trackFromBd = database
                .trackDao()
                .getTrack(trackId.toInt())
            tracksEntity.add(trackFromBd)
        }
        return converter.map(playlistEntity, tracksEntity)
    }
}