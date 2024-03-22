package com.example.playlistmaker.domain.api

import com.example.playlistmaker.ui.tracks.SearchResult

interface TracksInteractor {
    fun searchTracks(expression: String, consumer: TracksConsumer)

    interface TracksConsumer {
        fun consume(foundTracks: SearchResult)
    }
}
