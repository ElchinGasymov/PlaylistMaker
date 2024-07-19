package com.example.playlistmaker.di

import com.example.playlistmaker.media_library.domain.db.FavoriteTrackInteractor
import com.example.playlistmaker.media_library.domain.db.PlaylistsInteractor
import com.example.playlistmaker.media_library.domain.impl.FavoriteTrackInteractorImpl
import com.example.playlistmaker.media_library.domain.impl.PlaylistsInteractorImpl
import com.example.playlistmaker.new_playlist.domain.NewPlaylistInteractor
import com.example.playlistmaker.new_playlist.domain.impl.NewPlaylistInteractorImpl
import com.example.playlistmaker.player.domain.PlayerInteractor
import com.example.playlistmaker.player.domain.impl.PlayerInteractorImpl
import com.example.playlistmaker.search.domain.TracksInteractor
import com.example.playlistmaker.search.domain.impl.TracksInteractorImpl
import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import org.koin.dsl.module

val interactorModule = module {
    factory<PlayerInteractor> {
        PlayerInteractorImpl(repository = get())
    }

    factory<TracksInteractor> {
        TracksInteractorImpl(repository = get())
    }

    factory<SettingsInteractor> {
        SettingsInteractorImpl(repository = get())
    }

    factory<FavoriteTrackInteractor> {
        FavoriteTrackInteractorImpl(repository = get())
    }

    factory<NewPlaylistInteractor> {
        NewPlaylistInteractorImpl(repository = get())
    }

    factory<PlaylistsInteractor> {
        PlaylistsInteractorImpl(repository = get())
    }
}
