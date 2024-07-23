package com.example.playlistmaker.playlist.presentation.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media_library.domain.db.PlaylistsInteractor
import com.example.playlistmaker.new_playlist.domain.model.Playlist
import com.example.playlistmaker.settings.domain.SettingsInteractor
import kotlinx.coroutines.launch

class PlaylistMenuViewModel(
    private val playlistsInteractor: PlaylistsInteractor,
    private val settingsInteractor: SettingsInteractor
) : ViewModel() {

    fun deletePlaylist(playlist: Playlist) {
        viewModelScope.launch {
            playlistsInteractor.deletePlaylist(playlist)
        }
    }

    fun isDarkModeOn(): Boolean {
        return settingsInteractor.isDarkModeOn()
    }
}