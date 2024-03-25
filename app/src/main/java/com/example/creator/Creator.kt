package com.example.creator

import android.media.MediaPlayer
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.data.repository.TrackRepositoryImpl
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.impl.PlayerInteractorImpl
import com.example.playlistmaker.domain.impl.TracksInteractorImpl
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.repository.TracksRepository
import com.example.playlistmaker.domain.use_case.player.PlayerInteractor
import com.example.playlistmaker.presentation.player.PlayerPresenter
import com.example.playlistmaker.ui.converter.SongConverter

object Creator {
    private fun getTracksRepository(): TracksRepository {
        return TrackRepositoryImpl(RetrofitNetworkClient(), SongConverter())
    }

    fun provideMoviesInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }

    private fun providePlayerInteractor(track: Track, mediaPlayer: MediaPlayer): PlayerInteractor {
        return PlayerInteractorImpl(track, mediaPlayer)
    }

    fun providePlayerPresenter(track: Track, mediaPlayer: MediaPlayer): PlayerPresenter {
        return PlayerPresenter(providePlayerInteractor(track, mediaPlayer), track)
    }
}
