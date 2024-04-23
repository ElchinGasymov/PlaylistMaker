package com.example.playlistmaker.settings.ui.view_model

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.App

class SettingsViewModel(application: App) : AndroidViewModel(application) {

    private val switchThemeInteractor = application.themeSwitcherInteractor

    private val _isDarkModeOn = MutableLiveData<Boolean>()
    var isDarkModeOn: LiveData<Boolean> = _isDarkModeOn

    fun switchTheme(isChecked: Boolean) {
        switchThemeInteractor.switch(isChecked)
    }

    fun isDarkThemeOn() {
        _isDarkModeOn.value = switchThemeInteractor.isDarkModeOn()
    }

    companion object {
        fun getViewModelFactory(application: App): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return SettingsViewModel(
                        application = application
                    ) as T
                }
            }
    }
}
