package com.example.playlistmaker.media_library.ui.view_model

import com.example.playlistmaker.new_playlist.domain.model.Playlist

sealed class PlaylistsScreenState {

    data object Empty : PlaylistsScreenState()

    class Content(val playlists: List<Playlist>) : PlaylistsScreenState()
}