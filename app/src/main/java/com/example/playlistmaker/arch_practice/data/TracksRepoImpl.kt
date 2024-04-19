package com.example.playlistmaker.arch_practice.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.playlistmaker.arch_practice.domain.TracksRepo

class TracksRepoImpl:TracksRepo {

    var liveData = MutableLiveData(0)

    override fun getTracksFromMock(): List<String> {
        return listOf("song1", "song2", "song3")
    }

    override fun getLiveData(): LiveData<Int> {
        return liveData
    }

    //тут должен быть метод типа setLiveData для обновления значения?
}
