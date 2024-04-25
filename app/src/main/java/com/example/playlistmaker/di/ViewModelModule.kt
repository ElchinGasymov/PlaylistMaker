package com.example.playlistmaker.di

import com.example.playlistmaker.player.ui.view_model.AudioPlayerViewModel
import com.example.playlistmaker.search.ui.view_model.SearchViewModel
import com.example.playlistmaker.settings.ui.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        AudioPlayerViewModel(playerInteractor = get())
    }

    viewModel {
        SearchViewModel(tracksInteractor = get())
    }

    viewModel {
        SettingsViewModel(switchThemeInteractor = get())
    }
}