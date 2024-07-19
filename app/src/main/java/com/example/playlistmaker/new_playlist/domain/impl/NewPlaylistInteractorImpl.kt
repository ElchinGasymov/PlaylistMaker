package com.example.playlistmaker.new_playlist.domain.impl

import com.example.playlistmaker.media_library.domain.db.PlaylistsRepository
import com.example.playlistmaker.new_playlist.domain.NewPlaylistInteractor
import com.example.playlistmaker.new_playlist.domain.model.Playlist

class NewPlaylistInteractorImpl(
    private val repository: PlaylistsRepository
) : NewPlaylistInteractor {
    override suspend fun createPlaylist(playlist: Playlist) {
        repository.createPlaylist(playlist)
    }
}