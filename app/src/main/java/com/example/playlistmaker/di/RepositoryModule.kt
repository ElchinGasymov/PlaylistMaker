package com.example.playlistmaker.di

import com.example.playlistmaker.media_library.data.impl.FavouriteTrackRepositoryImpl
import com.example.playlistmaker.media_library.domain.db.FavouriteTrackRepository
import com.example.playlistmaker.player.data.impl.PlayerRepositoryImpl
import com.example.playlistmaker.player.domain.PlayerRepository
import com.example.playlistmaker.search.data.impl.TrackRepositoryImpl
import com.example.playlistmaker.search.domain.TracksRepository
import com.example.playlistmaker.settings.data.impl.SettingsRepositoryImpl
import com.example.playlistmaker.settings.domain.SettingsRepository
import org.koin.dsl.module

val repositoryModule = module {
    factory<PlayerRepository> {
        PlayerRepositoryImpl(mediaPlayer = get())
    }

    single<TracksRepository> {
        TrackRepositoryImpl(
            networkClient = get(),
            historyLocalStorage = get(),
            songConverter = get(),
            database = get()
        )
    }

    factory<SettingsRepository> {
        SettingsRepositoryImpl(themeStorage = get())
    }

    //factory { TrackDbConvertor() }

    single<FavouriteTrackRepository> {
        FavouriteTrackRepositoryImpl(
            database = get(),
            converter = get()
        )
    }
}
