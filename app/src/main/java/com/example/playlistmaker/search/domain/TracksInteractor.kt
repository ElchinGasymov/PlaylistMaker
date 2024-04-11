package com.example.playlistmaker.search.domain

interface TracksInteractor {
    fun searchTracks(expression: String, consumer: TracksConsumer)

    interface TracksConsumer {
        fun consume(foundTracks: List<Track>?, errorMessage: String?)
    }

    fun addTrackToHistory(track: Track)
    fun clearHistory()
    fun getHistory(): List<Track>
}
