package com.example.playlistmaker.ui.tracks

import com.example.playlistmaker.domain.model.Track

sealed interface SearchResult {
    data class Data(val trackList: List<Track>) : SearchResult
    object Empty : SearchResult
    object Error : SearchResult
    object Loading : SearchResult
}
