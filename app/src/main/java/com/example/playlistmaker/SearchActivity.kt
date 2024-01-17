package com.example.playlistmaker

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.adapter.TracksAdapter
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

    private val baseUrl = "https://itunes.apple.com"

    private val service = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create<SongsApiService>()

    val songConverter = SongConverter()

    companion object {
        private const val EMPTY = ""
    }

    private var inputText: String = ""
    private val SEARCH_QUERY_KEY = "searchQuery"

    val trackList: MutableList<Track> = mutableListOf()
    val tracksAdapter = TracksAdapter(trackList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val inputEditText = findViewById<EditText>(R.id.searchInputTextField)
        val clearButton = findViewById<ImageView>(R.id.clearButton)
        val toolbar = findViewById<MaterialToolbar>(R.id.searchToolbar)
        val recyclerView = findViewById<RecyclerView>(R.id.trackRecyclerView)
        val nothingToShowView = findViewById<LinearLayout>(R.id.nothing_to_show)
        val networkProblemsView = findViewById<LinearLayout>(R.id.network_problems)
        val updateButton = findViewById<TextView>(R.id.update)

        recyclerView.adapter = tracksAdapter

        if (savedInstanceState != null) {
            inputEditText.setText(inputText)
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.isVisible = !s.isNullOrEmpty()
            }

            override fun afterTextChanged(s: Editable) {
                inputText = s.toString()
            }

        }
        inputEditText.addTextChangedListener(simpleTextWatcher)

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchSongs(inputEditText.text.toString(), recyclerView, nothingToShowView, networkProblemsView)
                hideKeyboard()
                return@setOnEditorActionListener true
            }
            false
        }

        updateButton.setOnClickListener {
            searchSongs(inputEditText.text.toString(), recyclerView, nothingToShowView, networkProblemsView)
            hideKeyboard()
        }

        clearButton.setOnClickListener {
            inputEditText.setText("")
            hideKeyboard()
            recyclerView.isVisible = false
            nothingToShowView.isVisible = false
            networkProblemsView.isVisible = false
        }

        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

    }

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

    private fun searchSongs(
        query: String,
        recyclerView: RecyclerView,
        nothingToShowView: View,
        networkProblemsView: View,
    ) {
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
                        recyclerView.isVisible = false
                        nothingToShowView.isVisible = true
                        networkProblemsView.isVisible = false
                    } else {
                        recyclerView.isVisible = true
                        nothingToShowView.isVisible = false
                        networkProblemsView.isVisible = false
                    }
                } else {
                    recyclerView.isVisible = false
                    nothingToShowView.isVisible = false
                    networkProblemsView.isVisible = true
                }
            }

            override fun onFailure(call: Call<SongsResponse>, t: Throwable) {
                recyclerView.isVisible = false
                nothingToShowView.isVisible = false
                networkProblemsView.isVisible = true
            }
        })
    }



}
