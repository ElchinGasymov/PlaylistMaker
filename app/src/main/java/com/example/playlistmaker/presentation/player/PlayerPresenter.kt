package com.example.playlistmaker.presentation.player

import android.os.Handler
import android.os.Looper
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.Constants.Companion.DELAY_MILLIS
import com.example.playlistmaker.domain.Constants.Companion.STATE_PAUSED
import com.example.playlistmaker.domain.Constants.Companion.STATE_PLAYING
import com.example.playlistmaker.domain.Constants.Companion.STATE_PREPARED
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.use_case.player.PlayerInteractor
import com.example.playlistmaker.ui.converter.DurationConverter

class PlayerPresenter(private val player: PlayerInteractor, val track: Track) {
    private var view: PlayerView? = null
    private var timerHandler = Handler(Looper.getMainLooper())
    private val durationConverter = DurationConverter()

    fun attachView(view: PlayerView) {
        this.view = view
        view.setTrackName(track.trackName)
        view.setArtistName(track.artistName)
        view.setTrackTime(track.trackTime)
        view.setArtwork(track.getCoverArtwork())
        view.setCollection(track.collectionName)
        view.setReleaseDate(track.getYearFromDate())
        view.setPrimaryGenre(track.primaryGenreName)
        view.setCountry(track.country)
        player.setCompletionListener {
            view.setPlayButtonImage((R.drawable.ic_play))
            timerHandler.removeCallbacks(timerRunnable)
            view.setPlayTimeText(durationConverter.milsToMinSec(0))
        }
    }

    fun detachView() {
        this.view = null
        timerHandler.removeCallbacksAndMessages(null)
        player.releasePlayer()
    }

    fun playbackControl() {
        when (player.getPlayerState()) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    fun startPlayer() {
        player.startPlayer()
        timerHandler.post(timerRunnable)
        view?.setPlayButtonImage(R.drawable.ic_pause)
    }

    fun pausePlayer() {
        player.pausePlayer()
        view?.setPlayButtonImage(R.drawable.ic_play)
        timerHandler.removeCallbacksAndMessages(null)
    }

    val timerRunnable = object : Runnable {
        override fun run() {
            view?.setPlayTimeText(durationConverter.milsToMinSec(player.getCurrentTime().toLong()))
            timerHandler.postDelayed(this, DELAY_MILLIS)
        }
    }
}
