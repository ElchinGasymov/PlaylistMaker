package com.example.playlistmaker.search.ui

import com.example.playlistmaker.search.domain.Track

sealed interface SearchScreenState {

    object Loading : SearchScreenState

    object NothingFound : SearchScreenState

    data class Success(
        val tracks: MutableList<Track>
    ) : SearchScreenState

    data class ShowHistory(
        val tracks: MutableList<Track>
    ) : SearchScreenState

    data class Error(
        val message: String
    ) : SearchScreenState

}
