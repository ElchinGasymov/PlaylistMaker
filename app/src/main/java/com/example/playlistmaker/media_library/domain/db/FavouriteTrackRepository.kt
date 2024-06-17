package com.example.playlistmaker.media_library.domain.db

import com.example.playlistmaker.search.domain.Track
import kotlinx.coroutines.flow.Flow

interface FavouriteTrackRepository {
    suspend fun likeTrack(track: Track)
    suspend fun unlikeTrack(track: Track)
    suspend fun getFavoritesTracks(): Flow<List<Track>>
}