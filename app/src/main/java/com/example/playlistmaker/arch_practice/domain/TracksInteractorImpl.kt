package com.example.playlistmaker.arch_practice.domain

import com.example.playlistmaker.arch_practice.data.TracksRepoImpl

class TracksInteractorImpl : TracksInteractor {

    private val tracksRepo = TracksRepoImpl()

    override fun getTracks(): String {
        return formatList(tracksRepo.getTracksFromMock())
    }

    private fun formatList(list: List<String>): String {
        return list.joinToString("\n")
    }
}