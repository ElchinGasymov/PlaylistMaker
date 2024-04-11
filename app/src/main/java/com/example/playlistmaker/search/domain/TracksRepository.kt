package com.example.playlistmaker.search.domain

import com.example.playlistmaker.utils.Resource

interface TracksRepository {
    fun searchTracks(query: String): Resource<List<Track>>
    fun addTrackToHistory(track: Track)
    fun clearHistory()
    fun getHistory(): List<Track>
}
