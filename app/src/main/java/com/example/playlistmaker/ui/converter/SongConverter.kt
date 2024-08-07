package com.example.playlistmaker.ui.converter

import com.example.playlistmaker.search.data.model.SongDto
import com.example.playlistmaker.search.domain.Track

class SongConverter(private val convertor: DurationConverter) {

    fun mapToUiModels(song: SongDto): Track {
        return Track(
            trackName = song.trackName?: "",
            artistName = song.artistName,
            trackTime = convertor.milsToMinSec(song.trackTimeMillis),
            artworkUrl100 = song.artworkUrl100,
            id = song.trackId,
            collectionName = song.collectionName?: "",
            releaseDate = song.releaseDate?: "",
            primaryGenreName = song.primaryGenreName?: "",
            country = song.country?: "",
            previewUrl = song.previewUrl?: ""
        )
    }
}
