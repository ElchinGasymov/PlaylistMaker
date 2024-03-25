package com.example.playlistmaker.domain.use_case.player

interface PlayerInteractor {
    fun startPlayer()
    fun pausePlayer()
    fun releasePlayer()
    fun getPlayerState(): Int
    fun getCurrentTime(): Int
    fun setCompletionListener(callback: () -> Unit)
}
