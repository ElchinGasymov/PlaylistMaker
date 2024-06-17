package com.example.playlistmaker.search.data

import com.example.playlistmaker.search.domain.Track

interface ILocalStorage {
    suspend fun addToHistory(track: Track)
    fun clearHistory()
    suspend fun getHistory(): List<Track>
}
