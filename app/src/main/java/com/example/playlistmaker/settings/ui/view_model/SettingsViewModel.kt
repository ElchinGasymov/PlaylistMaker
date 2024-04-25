package com.example.playlistmaker.settings.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.settings.domain.SettingsInteractor

class SettingsViewModel(private val switchThemeInteractor: SettingsInteractor) : ViewModel() {

    private val _isDarkModeOn = MutableLiveData<Boolean>()
    var isDarkModeOn: LiveData<Boolean> = _isDarkModeOn

    fun switchTheme(isChecked: Boolean) {
        switchThemeInteractor.switch(isChecked)
    }

    fun isDarkThemeOn() {
        _isDarkModeOn.value = switchThemeInteractor.isDarkModeOn()
    }

}
