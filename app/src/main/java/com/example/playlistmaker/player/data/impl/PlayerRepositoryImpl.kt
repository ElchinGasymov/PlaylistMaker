package com.example.playlistmaker.player.data.impl

import android.media.MediaPlayer
import com.example.playlistmaker.player.domain.PlayerRepository

class PlayerRepositoryImpl(private var mediaPlayer: MediaPlayer) : PlayerRepository {

    override fun preparePlayer(
        url: String,
        onPreparedListener: () -> Unit,
        onCompletionListener: () -> Unit
    ) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            onPreparedListener.invoke()
        }
        mediaPlayer.setOnCompletionListener {
            onCompletionListener.invoke()
        }
    }

    override fun startPlayer() {
        mediaPlayer.start()
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
    }

    override fun isPlaying(): Boolean {
        return mediaPlayer.isPlaying
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }


}
