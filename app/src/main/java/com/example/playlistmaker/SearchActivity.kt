package com.example.playlistmaker

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.adapter.TracksAdapter
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.model.Track
import com.example.playlistmaker.network.SongsApiService
import com.example.playlistmaker.network.SongsResponse
import com.example.playlistmaker.ui.converter.SongConverter
import com.google.android.material.appbar.MaterialToolbar
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
    }

    private val inputEditText: EditText by lazy { findViewById(R.id.searchInputTextField) }
    private val clearButton: ImageView by lazy { findViewById(R.id.clearButton) }
    private val toolbar: MaterialToolbar by lazy { findViewById(R.id.searchToolbar) }
    private val trackRecycler: RecyclerView by lazy { findViewById(R.id.trackRecyclerView) }
    private val nothingToShowView: LinearLayout by lazy { findViewById(R.id.nothing_to_show) }
    private val networkProblemsView: LinearLayout by lazy { findViewById(R.id.network_problems) }
    private val updateButton: TextView by lazy { findViewById(R.id.update) }
    private val historyRecycler: RecyclerView by lazy { findViewById(R.id.trackHistoryRecyclerView) }
    private val trackHistoryView: LinearLayout by lazy { findViewById(R.id.trackSearchHistory) }
    private val clearHistoryButton: TextView by lazy { findViewById(R.id.clear_history) }

    private lateinit var binding: ActivitySearchBinding

    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var searchHistory: SearchHistory
    private lateinit var historyAdapter: TracksAdapter

    private var inputText: String = ""

    private val baseUrl = "https://itunes.apple.com"

    private val service = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create<SongsApiService>()

    val songConverter = SongConverter()

    val trackList: MutableList<Track> = mutableListOf()
    val tracksAdapter = TracksAdapter(trackList) { clickedTrack ->
        onTrackClicked(clickedTrack)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setContentView(R.layout.activity_search)

        sharedPrefs = getSharedPreferences(PREFERENCES, MODE_PRIVATE)
        searchHistory = SearchHistory(sharedPrefs)
        historyAdapter = TracksAdapter(searchHistory.getSongsFromHistory().toMutableList()) {}

        trackRecycler.adapter = tracksAdapter
        historyRecycler.adapter = historyAdapter

        if (savedInstanceState != null) {
            inputEditText.setText(inputText)
        }

        inputEditText.doOnTextChanged{text, start, before, count ->
            clearButton.isVisible = !text.isNullOrEmpty()
            trackHistoryView.isVisible =
                inputEditText.hasFocus() && text?.isEmpty() == true && searchHistory.getSongsFromHistory().isNotEmpty()
        }

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchSongs(inputEditText.text.toString())
                hideKeyboard()
                return@setOnEditorActionListener true
            }
            false
        }

        updateButton.setOnClickListener {
            searchSongs(inputEditText.text.toString())
            hideKeyboard()
        }

        clearButton.setOnClickListener {
            inputEditText.setText("")
            hideKeyboard()
            trackRecycler.isVisible = false
            nothingToShowView.isVisible = false
            networkProblemsView.isVisible = false
            historyAdapter.setData(searchHistory.getSongsFromHistory())
        }

        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        inputEditText.setOnFocusChangeListener { _, hasFocus ->
            trackHistoryView.visibility =
                if (hasFocus && inputEditText.text.isEmpty() && searchHistory.getSongsFromHistory().isNotEmpty())
                    View.VISIBLE else View.GONE
        }

        clearHistoryButton.setOnClickListener {
            searchHistory.clearSongsHistory()
            historyAdapter.setData(searchHistory.getSongsFromHistory())
            trackHistoryView.isVisible = false
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
        service.getSongs(query).enqueue(object : Callback<SongsResponse> {
            override fun onResponse(call: Call<SongsResponse>, response: Response<SongsResponse>) {
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
                setUiByDataState(DataState.Error)
            }
        })
    }

    enum class DataState {
        Data,
        Empty,
        Error
    }

    private fun setUiByDataState(dataState: DataState) {
        trackRecycler.isVisible = dataState == DataState.Data
        nothingToShowView.isVisible = dataState == DataState.Empty
        networkProblemsView.isVisible = dataState == DataState.Error
    }

    private fun onTrackClicked(clickedTrack: Track) {
        Toast.makeText(this, getString(R.string.added_to_history), Toast.LENGTH_SHORT).show()
        searchHistory.addSongToHistory(clickedTrack)
    }

}
