package com.example.playlistmaker.player.ui.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.Constants
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.player.ui.PlayerScreenState
import java.text.SimpleDateFormat
import java.util.*

class AudioPlayerViewModel : ViewModel() {

    private val playerInteractor = Creator.providePlayerInteractor()

    private val stateLiveData = MutableLiveData<PlayerScreenState>()

    fun observeState(): LiveData<PlayerScreenState> = stateLiveData

    private val handler = Handler(Looper.getMainLooper())

    private val updatePlayingTimeRunnable = Runnable { updatePlayingTime() }

    fun preparePlayer(url: String) {
        renderState(PlayerScreenState.Preparing)
        playerInteractor.preparePlayer(
            url = url,
            onPreparedListener = {
                renderState(PlayerScreenState.Stopped)
            },
            onCompletionListener = {
                handler.removeCallbacks(updatePlayingTimeRunnable)
                renderState(PlayerScreenState.Stopped)
            }
        )
    }

    private fun startPlayer() {
        playerInteractor.startPlayer()
        renderState(PlayerScreenState.Playing)
        handler.postDelayed(updatePlayingTimeRunnable, Constants.DELAY_MILLIS)
    }

    fun pausePlayer() {
        playerInteractor.pausePlayer()
        renderState(PlayerScreenState.Paused)
        handler.removeCallbacks(updatePlayingTimeRunnable)
    }

    private fun isPlaying(): Boolean {
        return playerInteractor.isPlaying()
    }

    private fun getCurrentPosition(): Int {
        return playerInteractor.getCurrentPosition()
    }

    fun playbackControl() {
        if (isPlaying()) {
            pausePlayer()
        } else {
            startPlayer()
        }
    }

    private fun updatePlayingTime() {
        renderState(
            PlayerScreenState.UpdatePlayingTime(
                SimpleDateFormat(
                    "mm:ss",
                    Locale.getDefault()
                ).format(
                    getCurrentPosition()
                )
            )
        )
        handler.postDelayed(updatePlayingTimeRunnable, Constants.DELAY_MILLIS)
    }

    private fun renderState(state: PlayerScreenState) {
        stateLiveData.postValue(state)
    }


}