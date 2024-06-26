package com.example.playlistmaker.settings.data.impl

import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.settings.data.LocalStorage
import com.example.playlistmaker.settings.domain.SettingsRepository

class SettingsRepositoryImpl(private val themeStorage: LocalStorage) : SettingsRepository {
    override fun switchTheme(darkThemeEnabled: Boolean) {
        themeStorage.switch(darkThemeEnabled)
        applyCurrentTheme()
    }

    override fun isDarkModeOn(): Boolean {
        return themeStorage.isDarkModeOn()
    }

    override fun applyCurrentTheme() {
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkModeOn()) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}
