package com.example.playlistmaker

import android.content.SharedPreferences
import com.example.playlistmaker.model.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistory(private val sharedPrefs: SharedPreferences) {

    companion object {
        private const val HISTORY_KEY = "history"
        private const val MAX_HISTORY_SIZE = 10
    }

    fun getSongsFromHistory(): List<Track> {
        val jsonHistory = sharedPrefs.getString(HISTORY_KEY, null)
        return if (!jsonHistory.isNullOrEmpty()) {
            val type = object : TypeToken<List<Track>>() {}.type
            Gson().fromJson(jsonHistory, type)
        } else {
            emptyList()
        }
    }

    fun addSongToHistory(track: Track) {
        val currentHistory = getSongsFromHistory().toMutableList()

        currentHistory.removeAll { it.trackId == track.trackId }

        currentHistory.add(0, track)

        if (currentHistory.size > MAX_HISTORY_SIZE) {
            currentHistory.subList(MAX_HISTORY_SIZE, currentHistory.size).clear()
        }

        saveHistory(currentHistory)
    }

    fun clearSongsHistory() {
        sharedPrefs.edit().remove(HISTORY_KEY).apply()
    }

    private fun saveHistory(history: List<Track>) {
        val jsonHistory = Gson().toJson(history)
        sharedPrefs.edit().putString(HISTORY_KEY, jsonHistory).apply()
    }
}
