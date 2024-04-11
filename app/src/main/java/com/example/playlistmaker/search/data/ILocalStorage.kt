package com.example.playlistmaker.search.data

import com.example.playlistmaker.search.domain.Track

interface ILocalStorage {
    fun addToHistory(track: Track)
    fun clearHistory()
    fun getHistory(): List<Track>
}
