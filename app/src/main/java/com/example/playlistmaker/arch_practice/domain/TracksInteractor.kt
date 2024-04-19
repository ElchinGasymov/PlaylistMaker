package com.example.playlistmaker.arch_practice.domain

interface TracksInteractor {
    fun getTracks(): String
    fun liveDataPlusOne(): Int
    fun liveDataMinusOne(): Int
}
