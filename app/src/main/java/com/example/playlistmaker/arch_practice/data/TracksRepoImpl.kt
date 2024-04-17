package com.example.playlistmaker.arch_practice.data

import com.example.playlistmaker.arch_practice.domain.TracksRepo

class TracksRepoImpl:TracksRepo {
    override fun getTracksFromMock(): List<String> {
        return listOf("song1", "song2", "song3")
    }
}
