package com.example.playlistmaker.search.data.storage

import android.content.SharedPreferences
import com.example.playlistmaker.Constants.Companion.HISTORY_KEY
import com.example.playlistmaker.Constants.Companion.MAX_HISTORY_SIZE
import com.example.playlistmaker.search.data.ILocalStorage
import com.example.playlistmaker.search.domain.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class LocalStorage(private val preferences: SharedPreferences) : ILocalStorage {
    override fun addToHistory(track: Track) {
        val currentHistory = getHistory().toMutableList()

        currentHistory.removeAll { it.trackId == track.trackId }

        currentHistory.add(0, track)

        if (currentHistory.size > MAX_HISTORY_SIZE) {
            currentHistory.subList(MAX_HISTORY_SIZE, currentHistory.size).clear()
        }

        saveHistory(currentHistory)
    }

    override fun clearHistory() {
        preferences.edit().remove(HISTORY_KEY).apply()
    }

    override fun getHistory(): List<Track> {
        val jsonHistory = preferences.getString(HISTORY_KEY, null)
        return if (!jsonHistory.isNullOrEmpty()) {
            val type = object : TypeToken<List<Track>>() {}.type
            Gson().fromJson(jsonHistory, type)
        } else {
            emptyList()
        }
    }

    private fun saveHistory(history: List<Track>) {
        val jsonHistory = Gson().toJson(history)
        preferences.edit().putString(HISTORY_KEY, jsonHistory).apply()
    }

}
