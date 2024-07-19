package com.example.playlistmaker.media_library.ui.view_model

import com.example.playlistmaker.new_playlist.domain.model.Playlist

sealed class BottomSheetState(
    val content: List<Playlist> = emptyList(),
) {

    data object Empty : BottomSheetState()

    class AddedNow(val playlistModel: Playlist) : BottomSheetState()

    class AddedAlready(val playlistModel: Playlist) : BottomSheetState()

    class Content(playlists: List<Playlist>) : BottomSheetState(content = playlists)
}