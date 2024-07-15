package com.example.playlistmaker.media_library.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media_library.domain.db.PlaylistsInteractor
import com.example.playlistmaker.new_playlist.domain.model.Playlist
import com.example.playlistmaker.search.domain.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BottomSheetViewModel(
    private val playlistsInteractor: PlaylistsInteractor,
) : ViewModel() {

    private val _contentFlow: MutableStateFlow<BottomSheetState> =
        MutableStateFlow(BottomSheetState.Empty)
    val contentFlow: StateFlow<BottomSheetState> = _contentFlow

    init {
        fillData()
    }

    fun onPlaylistClicked(playlist: Playlist, track: Track) {
        if (playlistsInteractor.isTrackAlreadyExists(playlist, track)) {
            _contentFlow.value = BottomSheetState.AddedAlready(playlist)
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                playlistsInteractor.addTrackToPlaylist(playlist, track)
                _contentFlow.value = BottomSheetState.AddedNow(playlist)
            }
        }
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
            _contentFlow.value = BottomSheetState.Empty
        } else {
            _contentFlow.value = BottomSheetState.Content(playlists)
        }
    }

}
