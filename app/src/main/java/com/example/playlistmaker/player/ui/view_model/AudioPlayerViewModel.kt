package com.example.playlistmaker.player.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.Constants
import com.example.playlistmaker.media_library.domain.db.FavoriteTrackInteractor
import com.example.playlistmaker.player.domain.PlayerInteractor
import com.example.playlistmaker.player.ui.PlayerScreenState
import com.example.playlistmaker.search.domain.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerViewModel(
    private val playerInteractor: PlayerInteractor,
    private val favoriteTrackInteractor: FavoriteTrackInteractor
) : ViewModel() {

    private val stateLiveData = MutableLiveData<PlayerScreenState>()
    fun observeState(): LiveData<PlayerScreenState> = stateLiveData

    private val isFavoriteLiveData = MutableLiveData<Boolean>()
    fun observeFavoriteState(): LiveData<Boolean> = isFavoriteLiveData

    private var progressTimer: Job? = null

    fun preparePlayer(url: String) {
        renderState(PlayerScreenState.Preparing)
        playerInteractor.preparePlayer(
            url = url,
            onPreparedListener = {
                renderState(PlayerScreenState.Stopped)
            },
            onCompletionListener = {
                progressTimer?.cancel()
                renderState(PlayerScreenState.Stopped)
            }
        )
    }

    private fun startPlayer() {
        playerInteractor.startPlayer()
        renderState(PlayerScreenState.Playing)
        updatePlayingTime()
    }

    fun pausePlayer() {
        playerInteractor.pausePlayer()
        renderState(PlayerScreenState.Paused)
        progressTimer?.cancel()
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
        progressTimer = viewModelScope.launch {
            while (isPlaying()) {
                delay(Constants.DELAY_MILLIS)
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
            }
        }
    }

    private fun renderState(state: PlayerScreenState) {
        stateLiveData.postValue(state)
    }

    fun onFavoriteClicked(track: Track) {
        viewModelScope.launch {
            if (isFavoriteLiveData.value == false) {
                favoriteTrackInteractor.likeTrack(track)
                isFavoriteLiveData.value = true
            } else {
                favoriteTrackInteractor.unlikeTrack(track.id)
                isFavoriteLiveData.value = false
            }
        }
    }

    fun initLikeButton(isLiked: Boolean) {
        isFavoriteLiveData.value = isLiked
    }

    fun isTrackLiked(trackId: Int) {
        viewModelScope.launch {
            isFavoriteLiveData.value = favoriteTrackInteractor.getIsLiked(trackId)
        }
    }

}
