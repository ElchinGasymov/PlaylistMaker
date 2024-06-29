package com.example.playlistmaker.media_library.ui

import com.example.playlistmaker.search.domain.Track

sealed interface FavoritesState {
    data class Content(
        val tracks: List<Track>
    ) : FavoritesState

    data object Empty : FavoritesState
}