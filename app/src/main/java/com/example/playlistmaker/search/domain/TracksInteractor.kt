package com.example.playlistmaker.search.domain

import kotlinx.coroutines.flow.Flow

interface TracksInteractor {
    fun searchTracks(expression: String): Flow<Pair<List<Track>?, String?>>

    suspend fun addTrackToHistory(track: Track)
    fun clearHistory()
    suspend fun getHistory(): List<Track>
}
