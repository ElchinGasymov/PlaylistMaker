package com.example.playlistmaker.playlist.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media_library.domain.db.PlaylistsInteractor
import com.example.playlistmaker.new_playlist.domain.model.Playlist
import com.example.playlistmaker.settings.domain.SettingsInteractor
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val playlistsInteractor: PlaylistsInteractor,
    private val settingsInteractor: SettingsInteractor
) : ViewModel() {

    private val _playlist = MutableLiveData<Playlist>()
    val playlist : LiveData<Playlist> = _playlist

    fun deleteTrack(trackId: Int, playlist: Playlist) {
        viewModelScope.launch {
            playlistsInteractor.deleteTrack(trackId, playlist)
        }
    }

    fun isDarkModeOn(): Boolean {
        return settingsInteractor.isDarkModeOn()
    }

    fun getPlaylist(id: Int) {
        viewModelScope.launch {
            playlistsInteractor.getPlaylistById(id)
                .onEach {
                    Log.d("TAG", "getPlaylist: $it")
                    _playlist.value = it
                }
                .launchIn(viewModelScope)

        }
    }
}