package com.example.playlistmaker.search.data.impl

import com.example.playlistmaker.ApiConstants
import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.SongsSearchRequest
import com.example.playlistmaker.search.data.network.SongsSearchResponse
import com.example.playlistmaker.search.data.storage.LocalStorage
import com.example.playlistmaker.search.domain.Track
import com.example.playlistmaker.search.domain.TracksRepository
import com.example.playlistmaker.ui.converter.SongConverter
import com.example.playlistmaker.utils.Resource

class TrackRepositoryImpl(
    private val networkClient: NetworkClient,
    private val localStorage: LocalStorage,
    private val songConverter: SongConverter
    ) : TracksRepository {

    override fun searchTracks(query: String): Resource<List<Track>> {

        val response = networkClient.doRequest(SongsSearchRequest(query))

        when (response.resultCode) {
            ApiConstants.NO_INTERNET_CONNECTION_CODE -> {
                return Resource.Error(ApiConstants.INTERNET_CONNECTION_ERROR)
            }
            ApiConstants.SUCCESS_CODE -> {
               val tracks = (response as SongsSearchResponse).songs.map { song ->
                    songConverter.mapToUiModels(song = song)
                }
                return Resource.Success(tracks)
            }
            else -> {
                return Resource.Error(ApiConstants.SERVER_ERROR)
            }
        }
    }

    override fun addTrackToHistory(track: Track) {
        localStorage.addToHistory(track)
    }

    override fun clearHistory() {
        localStorage.clearHistory()
    }

    override fun getHistory(): List<Track> {
        return localStorage.getHistory()
    }
}
