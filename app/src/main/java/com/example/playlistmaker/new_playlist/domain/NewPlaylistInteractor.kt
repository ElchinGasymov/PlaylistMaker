package com.example.playlistmaker.new_playlist.domain

import com.example.playlistmaker.new_playlist.domain.model.Playlist

interface NewPlaylistInteractor {
    suspend fun createPlaylist(playlist: Playlist)
}
