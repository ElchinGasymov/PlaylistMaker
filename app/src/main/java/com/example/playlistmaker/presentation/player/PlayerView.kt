package com.example.playlistmaker.presentation.player

interface PlayerView {
    fun setTrackName(name: String)
    fun setArtistName(name: String)
    fun setTrackTime(time: String)
    fun setArtwork(url: String)
    fun setCollection(name: String)
    fun setReleaseDate(date: String)
    fun setPrimaryGenre(name: String)
    fun setCountry(name: String)
    fun setPlayButtonImage(resId: Int)
    fun setPlayTimeText(time: String)
}
