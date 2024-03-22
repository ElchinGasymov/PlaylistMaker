package com.example.playlistmaker.data.repository

import com.example.playlistmaker.data.dto.SongsSearchRequest
import com.example.playlistmaker.data.dto.SongsSearchResponse
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.repository.TracksRepository
import com.example.playlistmaker.ui.converter.SongConverter

class TrackRepositoryImpl(private val networkClient: NetworkClient, private val songConverter: SongConverter) : TracksRepository {
    override fun searchTracks(expression: String): List<Track> {
        val response = networkClient.doRequest(SongsSearchRequest(expression))
        return if (response.resultCode == 200) {
            (response as SongsSearchResponse).songs.map { song ->
                songConverter.mapToUiModels(song = song)
            }
        } else {
            emptyList()
        }
    }
}
