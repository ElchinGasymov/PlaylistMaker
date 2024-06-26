package com.example.playlistmaker.media_library.domain.impl

import com.example.playlistmaker.media_library.domain.db.FavoriteTrackInteractor
import com.example.playlistmaker.media_library.domain.db.FavouriteTrackRepository
import com.example.playlistmaker.search.domain.Track
import kotlinx.coroutines.flow.Flow

class FavoriteTrackInteractorImpl(
    private val repository: FavouriteTrackRepository
) : FavoriteTrackInteractor {
    override suspend fun likeTrack(track: Track) {
        repository.likeTrack(track)
    }

    override suspend fun unlikeTrack(trackId: Int) {
        repository.unlikeTrack(trackId)
    }

    override suspend fun getFavoritesTracks(): Flow<List<Track>> {
        return repository.getFavoritesTracks()
    }
}