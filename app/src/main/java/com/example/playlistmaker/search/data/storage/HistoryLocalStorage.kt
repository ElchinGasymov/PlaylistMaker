package com.example.playlistmaker.search.data.storage

import android.content.SharedPreferences
import com.example.playlistmaker.Constants.Companion.HISTORY_KEY
import com.example.playlistmaker.Constants.Companion.MAX_HISTORY_SIZE
import com.example.playlistmaker.media_library.data.db.AppDatabase
import com.example.playlistmaker.search.data.ILocalStorage
import com.example.playlistmaker.search.domain.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class HistoryLocalStorage(
    private val preferences: SharedPreferences,
    private val gson: Gson,
    private val database: AppDatabase
) : ILocalStorage {
    override suspend fun addToHistory(track: Track) {
        val currentHistory = getHistory().toMutableList()

        currentHistory.removeAll { it.id == track.id }

        currentHistory.add(0, track)

        if (currentHistory.size > MAX_HISTORY_SIZE) {
            currentHistory.subList(MAX_HISTORY_SIZE, currentHistory.size).clear()
        }

        saveHistory(currentHistory)
    }

    override fun clearHistory() {
        preferences.edit().remove(HISTORY_KEY).apply()
    }

    override suspend fun getHistory(): List<Track> {
        val jsonHistory = preferences.getString(HISTORY_KEY, null)
        val history: List<Track> = if (!jsonHistory.isNullOrEmpty()) {
            val type = object : TypeToken<List<Track>>() {}.type
            gson.fromJson(jsonHistory, type)
        } else {
            emptyList()
        }
        val favouriteIds: List<Int> = database.favouriteTrackDao().getIds()

        val updatedHistory = history.map { track ->
            track.copy(isFavorite = favouriteIds.contains(track.id))
        }

        return updatedHistory
    }

    private fun saveHistory(history: List<Track>) {
        val jsonHistory = gson.toJson(history)
        preferences.edit().putString(HISTORY_KEY, jsonHistory).apply()
    }

}
