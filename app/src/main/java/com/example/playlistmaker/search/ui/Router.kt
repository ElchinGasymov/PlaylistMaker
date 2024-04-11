package com.example.playlistmaker.search.ui

import android.app.Activity
import android.content.Intent
import com.example.playlistmaker.Constants
import com.example.playlistmaker.player.ui.activity.PlayerActivity
import com.example.playlistmaker.search.domain.Track

class Router(private val activity: Activity) {

    fun goBack() {
        activity.finish()
    }

    fun openAudioPlayer(track: Track) {
        val intent = Intent(activity, PlayerActivity::class.java).apply {
            putExtra(Constants.TRACK_KEY, track)
        }
        activity.startActivity(intent)
    }
}
