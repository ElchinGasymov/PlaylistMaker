package com.example.playlistmaker.search.ui

import android.content.Intent
import androidx.fragment.app.Fragment
import com.example.playlistmaker.Constants
import com.example.playlistmaker.player.ui.activity.PlayerActivity
import com.example.playlistmaker.search.domain.Track

class Router(private val fragment: Fragment) {

    fun goBack() {
        fragment.activity?.finish()
    }

    fun openAudioPlayer(track: Track) {
        val intent = Intent(fragment.activity, PlayerActivity::class.java).apply {
            putExtra(Constants.TRACK_KEY, track)
        }
        fragment.activity?.startActivity(intent)
    }
}
