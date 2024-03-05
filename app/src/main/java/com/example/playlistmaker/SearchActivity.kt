package com.example.playlistmaker

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
import com.example.playlistmaker.adapter.TracksAdapter
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.model.Track
import com.example.playlistmaker.network.SongsApiService
import com.example.playlistmaker.network.SongsResponse
import com.example.playlistmaker.ui.converter.SongConverter
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class SearchActivity : AppCompatActivity() {

    companion object {
        private const val EMPTY = ""
        private const val PREFERENCES = "songs_preferences"
        private const val SEARCH_QUERY_KEY = "searchQuery"
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private lateinit var binding: ActivitySearchBinding

    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var searchHistory: SearchHistory
    private lateinit var historyAdapter: TracksAdapter

    private var inputText: String = ""

    private val baseUrl = "https://itunes.apple.com"

    private val client = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()

    private val service = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
        .create<SongsApiService>()

    val songConverter = SongConverter()

    val trackList: MutableList<Track> = mutableListOf()
    val tracksAdapter = TracksAdapter(trackList) { clickedTrack ->
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
        historyAdapter = TracksAdapter(searchHistory.getSongsFromHistory().toMutableList()) { clickedTrack ->
            onTrackClicked(clickedTrack)
        }

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
        if (query.isNotEmpty()) {
            setUiByDataState(DataState.Loading)
            service.getSongs(query).enqueue(object : Callback<SongsResponse> {
                override fun onResponse(call: Call<SongsResponse>, response: Response<SongsResponse>) {
                    binding.progressBar.isVisible = false
                    if (response.code() == 200) {
                        trackList.clear()
                        if (response.body()?.songs?.isNotEmpty() == true) {
                            trackList.addAll(response.body()?.songs!!.map {
                                songConverter.mapToUiModels(it)
                            })
                            tracksAdapter.notifyDataSetChanged()
                        }
                        if (trackList.isEmpty()) {
                            setUiByDataState(DataState.Empty)
                        } else {
                            setUiByDataState(DataState.Data)
                        }
                    } else {
                        setUiByDataState(DataState.Error)
                    }
                }

                override fun onFailure(call: Call<SongsResponse>, t: Throwable) {
                    binding.progressBar.isVisible = false
                    setUiByDataState(DataState.Error)
                    t.printStackTrace()
                }
            })
        }
    }

    enum class DataState {
        Data,
        Empty,
        Error,
        Loading
    }

    private fun setUiByDataState(dataState: DataState) {
        binding.trackRv.isVisible = dataState == DataState.Data
        binding.nothingToShowLl.isVisible = dataState == DataState.Empty
        binding.networkProblemsLl.isVisible = dataState == DataState.Error
        binding.progressBar.isVisible = dataState == DataState.Loading
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
