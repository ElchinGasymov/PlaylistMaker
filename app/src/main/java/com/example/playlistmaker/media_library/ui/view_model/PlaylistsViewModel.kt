package com.example.playlistmaker.media_library.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media_library.domain.db.PlaylistsInteractor
import com.example.playlistmaker.new_playlist.domain.model.Playlist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PlaylistsViewModel(private val playlistsInteractor: PlaylistsInteractor) :
    ViewModel() {

    private val _contentFlow: MutableStateFlow<PlaylistsScreenState> =
        MutableStateFlow(PlaylistsScreenState.Empty)
    val contentFlow: StateFlow<PlaylistsScreenState> = _contentFlow

    var isClickable = true

    init {
        fillData()
    }

    private fun fillData() {
        viewModelScope.launch(Dispatchers.IO) {
            playlistsInteractor
                .getPlaylists()
                .collect { playlists ->
                    processResult(playlists)
                }
        }

    }

    private fun processResult(playlists: List<Playlist>) {
        if (playlists.isEmpty()) {
            _contentFlow.value = (PlaylistsScreenState.Empty)
        } else {
            _contentFlow.value = (PlaylistsScreenState.Content(playlists))
        }
    }

    fun onPlaylistClick() {
        isClickable = false
    }

}