package com.example.playlistmaker.media_library.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.Constants
import com.example.playlistmaker.media_library.domain.db.FavoriteTrackInteractor
import com.example.playlistmaker.media_library.ui.FavoritesState
import com.example.playlistmaker.search.domain.Track
import com.example.playlistmaker.utils.debounce
import kotlinx.coroutines.launch

class FavouriteTracksViewModel(
    private val favoriteTrackinteractor: FavoriteTrackInteractor
) : ViewModel() {

    private val contentStateLiveData = MutableLiveData<FavoritesState>()
    fun observeContentState(): LiveData<FavoritesState> = contentStateLiveData

    var isClickable = true

    private val trackOnClickDebounce =
        debounce<Boolean>(Constants.CLICK_DEBOUNCE_DELAY, viewModelScope, false) {
            isClickable = it
        }

    fun getFavoritesTracks() {
        viewModelScope.launch {
            favoriteTrackinteractor.getFavoritesTracks()
                .collect { favoritesTracks ->
                    processResult(favoritesTracks)
                }
        }
    }

    private fun processResult(trackList: List<Track>) {
        when {
            trackList.isEmpty() -> {
                contentStateLiveData.value = FavoritesState.Empty
            }

            else -> {
                contentStateLiveData.value = FavoritesState.Content(trackList)
            }
        }
    }

    fun onTrackClick() {
        isClickable = false
        trackOnClickDebounce(true)
    }
}