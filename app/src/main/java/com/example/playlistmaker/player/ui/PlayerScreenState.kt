package com.example.playlistmaker.player.ui

sealed interface PlayerScreenState {

    object Preparing : PlayerScreenState
    object Stopped : PlayerScreenState
    object Playing : PlayerScreenState
    object Paused : PlayerScreenState

    data class UpdatePlayingTime(
        val playingTime: String
    ) : PlayerScreenState
}
