package com.example.playlistmaker.media_library.data.db

import com.example.playlistmaker.media_library.data.db.entity.TrackEntity
import com.example.playlistmaker.search.domain.Track

class TrackDbConvertor {
    fun map(track: TrackEntity): Track {
        return Track(
            track.trackName,
            track.artistName,
            track.trackTime,
            track.artworkUrl100,
            track.trackId,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl,
            isFavorite = true
        )
    }

    fun map(track: Track): TrackEntity {
        return TrackEntity(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTime,
            track.artworkUrl100,
            track.collectionName,
            track.country,
            track.primaryGenreName,
            track.releaseDate,
            track.previewUrl,
            saveDate = System.currentTimeMillis()
        )
    }
}