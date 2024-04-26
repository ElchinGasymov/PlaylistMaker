package com.example.playlistmaker.settings.domain.impl

import com.example.playlistmaker.settings.domain.SettingsInteractor
import com.example.playlistmaker.settings.domain.SettingsRepository

class SettingsInteractorImpl(private val repository: SettingsRepository) : SettingsInteractor {
    override fun switch(isDarkModeOn: Boolean) {
        repository.switchTheme(isDarkModeOn)
    }

    override fun isDarkModeOn(): Boolean {
        return repository.isDarkModeOn()
    }

    override fun applyCurrentTheme() {
        repository.applyCurrentTheme()
    }
}
