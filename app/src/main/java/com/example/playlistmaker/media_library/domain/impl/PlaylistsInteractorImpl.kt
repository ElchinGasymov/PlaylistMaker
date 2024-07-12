package com.example.playlistmaker.media_library.domain.impl

import com.example.playlistmaker.media_library.domain.db.PlaylistsInteractor
import com.example.playlistmaker.media_library.domain.db.PlaylistsRepository
import com.example.playlistmaker.new_playlist.domain.model.Playlist
import com.example.playlistmaker.search.domain.Track
import kotlinx.coroutines.flow.Flow

class PlaylistsInteractorImpl(
    private val repository: PlaylistsRepository,
) : PlaylistsInteractor {

    override fun getPlaylists(): Flow<List<Playlist>> {
        return repository.getSavedPlaylists()
    }

    override fun isTrackAlreadyExists(playlist: Playlist, track: Track) =
        playlist.trackList.contains(track)

    override suspend fun addTrackToPlaylist(playlist: Playlist, track: Track) {
        playlist.trackList = playlist.trackList + track
        playlist.tracksCount = playlist.trackList.size
        repository.updateTracks(playlist)
    }
}