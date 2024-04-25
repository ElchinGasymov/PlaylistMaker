package com.example.playlistmaker.settings.data.preferencies

import android.content.SharedPreferences
import com.example.playlistmaker.Constants
import com.example.playlistmaker.settings.data.LocalStorage

class ThemeLocalStorage(private val sharedPreferences: SharedPreferences): LocalStorage {
    override fun switch(darkThemeEnabled: Boolean) {
        sharedPreferences
            .edit()
            .putBoolean(Constants.APP_THEME_KEY, darkThemeEnabled)
            .apply()
    }

    override fun isDarkModeOn(): Boolean {
        return sharedPreferences.getBoolean(Constants.APP_THEME_KEY, false)
    }
}
