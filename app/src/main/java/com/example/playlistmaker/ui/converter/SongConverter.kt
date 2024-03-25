package com.example.playlistmaker.ui.converter

import com.example.playlistmaker.data.dto.SongDto
import com.example.playlistmaker.domain.model.Track

class SongConverter {

    private val convertor = DurationConverter()

    fun mapToUiModels(song: SongDto): Track {
        return Track(
            trackName = song.trackName?: "",
            artistName = song.artistName,
            trackTime = convertor.milsToMinSec(song.trackTimeMillis),
            artworkUrl100 = song.artworkUrl100,
            trackId = song.trackId,
            collectionName = song.collectionName?: "",
            releaseDate = song.releaseDate?: "",
            primaryGenreName = song.primaryGenreName?: "",
            country = song.country?: "",
            previewUrl = song.previewUrl?: ""
        )
    }
}
