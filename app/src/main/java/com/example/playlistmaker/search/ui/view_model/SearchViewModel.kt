package com.example.playlistmaker.search.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.Constants
import com.example.playlistmaker.search.domain.Track
import com.example.playlistmaker.search.domain.TracksInteractor
import com.example.playlistmaker.search.ui.SearchScreenState
import com.example.playlistmaker.search.ui.SingleLiveEvent
import com.example.playlistmaker.utils.debounce
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SearchViewModel(private val tracksInteractor: TracksInteractor) : ViewModel() {

    private val _screenState = MutableLiveData<SearchScreenState>()
    private val showToast = SingleLiveEvent<String>()
    private val _trackIsClickable = MutableLiveData(true)
    var trackIsClickable: LiveData<Boolean> = _trackIsClickable
    private var searchJob: Job? = null
    private var currentSearchQuery: String? = null

    private val tracksSearchDebounce =
        debounce<String>(Constants.SEARCH_DEBOUNCE_DELAY, viewModelScope, true) { query ->
            getTracks(query)
        }

    private val trackOnClickDebounce =
        debounce<Boolean>(Constants.CLICK_DEBOUNCE_DELAY, viewModelScope, false) {
            _trackIsClickable.value = it
        }

    init {
        showHistory()
    }

    fun observeState(): LiveData<SearchScreenState> = _screenState
    fun observeShowToast(): LiveData<String> = showToast

    fun searchDebounce(query: String) {
        if (query.isNotEmpty() && query != currentSearchQuery) {
            tracksSearchDebounce(query)
        }
    }

    fun onSearchClicked(track: Track) {
        trackOnClickDebounce(true)
        addToHistory(track)
    }

    fun getTracks(query: String) {
        if (query.isNotEmpty() && query != currentSearchQuery) {
            renderState(SearchScreenState.Loading)
            currentSearchQuery = query

            searchJob = viewModelScope.launch {
                tracksInteractor.searchTracks(query)
                    .collect { pair ->
                        processResult(pair.first, pair.second)
                    }
            }
        }
    }

    private fun processResult(foundTracks: List<Track>?, errorMessage: String?) {
        val tracks = arrayListOf<Track>()
        if (foundTracks != null) {
            tracks.addAll(foundTracks)
        }

        when {
            errorMessage != null -> {
                renderState(SearchScreenState.Error(message = errorMessage))
            }

            tracks.isEmpty() -> {
                renderState(SearchScreenState.NothingFound)
            }

            else -> {
                renderState(SearchScreenState.Success(tracks = tracks))
            }
        }
    }

    private fun addToHistory(track: Track) {
        viewModelScope.launch {
            tracksInteractor.addTrackToHistory(track)
        }
    }

    fun clearSearch() {
        searchJob?.cancel()
        currentSearchQuery = null
        val historyTracks = showHistory()
        if (historyTracks.isNotEmpty()) {
            renderState(SearchScreenState.ShowHistory(historyTracks as MutableList<Track>))
        } else {
            renderState(SearchScreenState.Success(arrayListOf()))
        }
    }

    fun clearHistory() {
        tracksInteractor.clearHistory()
        _screenState.postValue(SearchScreenState.Success(arrayListOf()))

    }

    private fun showHistory() {
        viewModelScope.launch {
            val historyTracks = tracksInteractor.getHistory()
            if (historyTracks.isNotEmpty()) {
                renderState(SearchScreenState.ShowHistory(historyTracks as MutableList<Track>))
            } else {
                renderState(SearchScreenState.Success(arrayListOf()))
            }
        }
    }

    private fun renderState(state: SearchScreenState) {
        _screenState.postValue(state)
    }

}
