package com.example.playlistmaker.di

import com.example.playlistmaker.media_library.ui.view_model.BottomSheetViewModel
import com.example.playlistmaker.media_library.ui.view_model.FavouriteTracksViewModel
import com.example.playlistmaker.media_library.ui.view_model.PlaylistsViewModel
import com.example.playlistmaker.new_playlist.ui.view_model.NewPlaylistViewModel
import com.example.playlistmaker.player.ui.view_model.AudioPlayerViewModel
import com.example.playlistmaker.search.ui.view_model.SearchViewModel
import com.example.playlistmaker.settings.ui.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        AudioPlayerViewModel(
            playerInteractor = get(),
            favoriteTrackInteractor = get()
        )
    }

    viewModel {
        SearchViewModel(tracksInteractor = get())
    }

    viewModel {
        SettingsViewModel(switchThemeInteractor = get())
    }

    viewModel {
        FavouriteTracksViewModel(favoriteTrackinteractor = get())
    }

    viewModel {
        NewPlaylistViewModel(
            newPlaylistInteractor = get(),
            settingsInteractor = get()
        )
    }

    viewModel {
        PlaylistsViewModel(playlistsInteractor = get())
    }

    viewModel {
        BottomSheetViewModel(playlistsInteractor = get())
    }
}