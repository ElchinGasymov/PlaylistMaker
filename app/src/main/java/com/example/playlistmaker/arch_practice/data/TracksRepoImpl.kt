package com.example.playlistmaker.arch_practice.data

import com.example.playlistmaker.arch_practice.domain.TracksRepo

class TracksRepoImpl:TracksRepo {

    private var data = 0

    override fun getTracksFromMock(): List<String> {
        return listOf("song1", "song2", "song3")
    }

    override fun getNewData(): Int {
        data += 1
        return data
    }

}
