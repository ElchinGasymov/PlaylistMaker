package com.example.playlistmaker.search.domain

import com.example.playlistmaker.utils.Resource
import kotlinx.coroutines.flow.Flow

interface TracksRepository {
    fun searchTracks(query: String): Flow<Resource<List<Track>>>
    suspend fun addTrackToHistory(track: Track)
    fun clearHistory()
    suspend fun getHistory(): List<Track>
}
