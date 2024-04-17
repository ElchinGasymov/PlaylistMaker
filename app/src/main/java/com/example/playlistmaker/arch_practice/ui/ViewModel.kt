package com.example.playlistmaker.arch_practice.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.playlistmaker.arch_practice.domain.TracksInteractorImpl

class ViewModel(application: Application) : AndroidViewModel(application) {
    private var interactor = TracksInteractorImpl()

    fun getTracks(): String {
        return interactor.getTracks()
    }

}
