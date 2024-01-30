package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {

    companion object {
        private const val PREFERENCES = "playlistmaker_preferences"
        private const val APP_THEME_KEY = "key_for_app_theme"
    }

    var darkTheme = false

    override fun onCreate() {
        super.onCreate()
        val preferences = getSharedPreferences(PREFERENCES, MODE_PRIVATE)
        darkTheme = preferences.getBoolean(APP_THEME_KEY, false)
        switchTheme(darkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

}
