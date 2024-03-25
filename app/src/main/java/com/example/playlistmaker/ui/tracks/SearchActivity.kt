package com.example.playlistmaker.ui.tracks

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import com.example.creator.Creator
import com.example.playlistmaker.Constants
import com.example.playlistmaker.SearchHistory
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.ui.player.PlayerActivity

class SearchActivity : AppCompatActivity() {

    companion object {
        private const val EMPTY = ""
        private const val PREFERENCES = "songs_preferences"
        private const val SEARCH_QUERY_KEY = "searchQuery"
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private val provideMoviesInteractor = Creator.provideMoviesInteractor()

    private lateinit var binding: ActivitySearchBinding

    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var searchHistory: SearchHistory
    private lateinit var historyAdapter: TracksAdapter

    private var inputText: String = ""

    private val tracksAdapter = TracksAdapter { clickedTrack ->
        searchHistory.addSongToHistory(clickedTrack)
        onTrackClicked(clickedTrack)
    }
    private val searchRunnable = Runnable { searchSongs(binding.searchSongEt.text.toString()) }

    private val handler = Handler(Looper.getMainLooper())

    private var isClickAllowed = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPrefs = getSharedPreferences(PREFERENCES, MODE_PRIVATE)
        searchHistory = SearchHistory(sharedPrefs)
        historyAdapter = TracksAdapter { clickedTrack ->
            onTrackClicked(clickedTrack)
        }
        historyAdapter.setData(searchHistory.getSongsFromHistory())

        binding.trackRv.adapter = tracksAdapter
        binding.trackHistoryRv.adapter = historyAdapter

        if (savedInstanceState != null) {
            binding.searchSongEt.setText(inputText)
        }

        binding.searchSongEt.doOnTextChanged { text, start, before, count ->
            binding.clearSearchBtn.isVisible = !text.isNullOrEmpty()
            binding.trackSearchHistoryLl.isVisible =
                binding.searchSongEt.hasFocus() && text?.isEmpty() == true && searchHistory.getSongsFromHistory()
                    .isNotEmpty()
            searchDebounce()
        }

        binding.searchSongEt.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchSongs(binding.searchSongEt.text.toString())
                hideKeyboard()
                return@setOnEditorActionListener true
            }
            false
        }

        binding.tryAgainBtn.setOnClickListener {
            searchSongs(binding.searchSongEt.text.toString())
            hideKeyboard()
        }

        binding.clearSearchBtn.setOnClickListener {
            binding.searchSongEt.setText("")
            hideKeyboard()
            binding.trackRv.isVisible = false
            binding.nothingToShowLl.isVisible = false
            binding.networkProblemsLl.isVisible = false
            historyAdapter.setData(searchHistory.getSongsFromHistory())
        }

        binding.searchToolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.searchSongEt.setOnFocusChangeListener { _, hasFocus ->
            binding.trackSearchHistoryLl.visibility =
                if (hasFocus && binding.searchSongEt.text.isEmpty() && searchHistory.getSongsFromHistory().isNotEmpty())
                    View.VISIBLE else View.GONE
        }

        binding.clearHistoryBtn.setOnClickListener {
            searchHistory.clearSongsHistory()
            historyAdapter.setData(searchHistory.getSongsFromHistory())
            binding.trackSearchHistoryLl.isVisible = false
        }

    } //конец onCreate!

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_QUERY_KEY, inputText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val savedSearchQuery = savedInstanceState.getString(SEARCH_QUERY_KEY) ?: EMPTY
        inputText = savedSearchQuery
    }

    private fun hideKeyboard() {
        val view: View? = this.currentFocus
        if (view != null) {
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun searchSongs(query: String) {
        provideMoviesInteractor.searchTracks(query, object : TracksInteractor.TracksConsumer{
            override fun consume(foundTracks: SearchResult) {
                handler.post { setUiByDataState(foundTracks) }
            }
        })
    }

    private fun setUiByDataState(dataState: SearchResult) {
        binding.trackRv.isVisible = dataState is SearchResult.Data
        binding.nothingToShowLl.isVisible = dataState is SearchResult.Empty
        binding.networkProblemsLl.isVisible = dataState is SearchResult.Error
        binding.progressBar.isVisible = dataState is SearchResult.Loading
        tracksAdapter.setData((dataState as? SearchResult.Data)?.trackList?: emptyList())
    }

    private fun onTrackClicked(clickedTrack: Track) {
        if (clickDebounce()) {
            val playerIntent = Intent(this, PlayerActivity::class.java)
            playerIntent.putExtra(Constants.TRACK_KEY, clickedTrack)
            startActivity(playerIntent)
        }
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

}
