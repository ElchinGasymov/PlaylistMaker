package com.example.playlistmaker.media_library.data.impl

import android.util.Log
import com.example.playlistmaker.media_library.data.db.AppDatabase
import com.example.playlistmaker.media_library.data.db.TrackDbConvertor
import com.example.playlistmaker.media_library.data.db.entity.TrackEntity
import com.example.playlistmaker.media_library.domain.db.FavouriteTrackRepository
import com.example.playlistmaker.search.domain.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavouriteTrackRepositoryImpl(
    private val database: AppDatabase,
    private val converter: TrackDbConvertor,
) : FavouriteTrackRepository {
    override suspend fun likeTrack(track: Track) {
        Log.d("TAG", "likeTrack: Repository")
        database
            .trackDao()
            .addTracks(converter.map(track))
    }

    override suspend fun unlikeTrack(track: Track) {
        Log.d("TAG", "unlikeTrack: Repository")
        database
            .trackDao()
            .deleteTrackEntity(converter.map(track))
    }

    override suspend fun getFavoritesTracks(): Flow<List<Track>> = flow {
        val tracks = database
            .trackDao().getTracks()
        emit(convertFromTrackEntity(tracks))
    }

    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { track -> converter.map(track) }
    }
}