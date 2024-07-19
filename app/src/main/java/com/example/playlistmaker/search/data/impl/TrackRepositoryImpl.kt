package com.example.playlistmaker.search.data.impl

import com.example.playlistmaker.ApiConstants
import com.example.playlistmaker.media_library.data.db.AppDatabase
import com.example.playlistmaker.search.data.ILocalStorage
import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.SongsSearchRequest
import com.example.playlistmaker.search.data.network.SongsSearchResponse
import com.example.playlistmaker.search.domain.Track
import com.example.playlistmaker.search.domain.TracksRepository
import com.example.playlistmaker.ui.converter.SongConverter
import com.example.playlistmaker.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TrackRepositoryImpl(
    private val networkClient: NetworkClient,
    private val historyLocalStorage: ILocalStorage,
    private val songConverter: SongConverter,
    private val database: AppDatabase
) : TracksRepository {

    override fun searchTracks(query: String): Flow<Resource<List<Track>>> = flow {

        val response = networkClient.doRequest(SongsSearchRequest(query))

        when (response.resultCode) {
            ApiConstants.NO_INTERNET_CONNECTION_CODE -> {
                emit(Resource.Error(ApiConstants.INTERNET_CONNECTION_ERROR))
            }

            ApiConstants.SUCCESS_CODE -> {
                val tracks = (response as SongsSearchResponse).songs.map { song ->
                    songConverter.mapToUiModels(song = song)
                }
                val favouriteIds = database.favouriteTrackDao().getIds()

                val updatedTracks = tracks.map { track ->
                    track.copy(isFavorite = favouriteIds.contains(track.id))
                }

                emit(Resource.Success(updatedTracks))
            }

            else -> {
                emit(Resource.Error(ApiConstants.SERVER_ERROR))
            }
        }
    }

    override suspend fun addTrackToHistory(track: Track) {
        historyLocalStorage.addToHistory(track)
    }

    override fun clearHistory() {
        historyLocalStorage.clearHistory()
    }

    override suspend fun getHistory(): List<Track> {
        return historyLocalStorage.getHistory()
    }
}
