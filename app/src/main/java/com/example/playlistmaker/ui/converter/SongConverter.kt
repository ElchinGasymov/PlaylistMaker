package com.example.playlistmaker.ui.converter

import com.example.playlistmaker.model.Track
import com.example.playlistmaker.network.Song

class SongConverter {

    private val convertor = DurationConverter()

    fun mapToUiModels(song: Song): Track {
        return Track(
            trackName = song.trackName?: "",
            artistName = song.artistName,
            trackTime = convertor.milsToMinSec(song.trackTimeMillis),
            artworkUrl100 = song.artworkUrl100,
            trackId = song.trackId
        )
    }
}
