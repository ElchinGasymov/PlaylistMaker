package com.example.playlistmaker.arch_practice.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.playlistmaker.arch_practice.domain.TracksInteractorImpl

class ViewModel(application: Application) : AndroidViewModel(application) {
    private var interactor = TracksInteractorImpl()
    internal var liveData = MutableLiveData(0)

    fun getTracks(): String {
        return interactor.getTracks()
    }

    fun liveDataPlusOne(): Int {
        return interactor.liveDataPlusOne()
    }

    fun liveDataMinusOne(): Int {
        return interactor.liveDataMinusOne()
    }

}
