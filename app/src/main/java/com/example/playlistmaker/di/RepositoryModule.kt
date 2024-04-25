package com.example.playlistmaker.di

import com.example.playlistmaker.player.data.impl.PlayerRepositoryImpl
import com.example.playlistmaker.player.domain.PlayerRepository
import com.example.playlistmaker.search.data.impl.TrackRepositoryImpl
import com.example.playlistmaker.search.domain.TracksRepository
import com.example.playlistmaker.settings.data.impl.SettingsRepositoryImpl
import com.example.playlistmaker.settings.domain.SettingsRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<PlayerRepository> {
        PlayerRepositoryImpl(mediaPlayer = get())
    }

    single<TracksRepository> {
        TrackRepositoryImpl(
            networkClient = get(),
            historyLocalStorage = get(),
            songConverter = get()
        )
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(themeStorage = get())
    }
}
