package com.example.playlistmaker.arch_practice.domain

import androidx.lifecycle.LiveData

interface TracksRepo {
    fun getTracksFromMock(): List<String>
    fun getLiveData(): LiveData<Int>
}
