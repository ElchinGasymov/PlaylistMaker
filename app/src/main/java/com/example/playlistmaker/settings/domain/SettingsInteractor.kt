package com.example.playlistmaker.settings.domain

interface SettingsInteractor {
    fun switch(isDarkModeOn: Boolean)
    fun isDarkModeOn(): Boolean
    fun applyCurrentTheme()
}
