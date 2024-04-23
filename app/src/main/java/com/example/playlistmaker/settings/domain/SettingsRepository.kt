package com.example.playlistmaker.settings.domain

interface SettingsRepository {
    fun switchTheme(darkThemeEnabled: Boolean)
    fun isDarkModeOn(): Boolean
    fun applyCurrentTheme()
}
