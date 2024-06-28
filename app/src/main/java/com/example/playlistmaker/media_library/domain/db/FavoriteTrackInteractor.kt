package com.example.playlistmaker.media_library.domain.db

import com.example.playlistmaker.search.domain.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteTrackInteractor {
    suspend fun likeTrack(track: Track)
    suspend fun unlikeTrack(trackId: Int)
    suspend fun getFavoritesTracks(): Flow<List<Track>>
    suspend fun getIsLiked(trackId: Int): Boolean
}