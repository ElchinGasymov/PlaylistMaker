package com.example.playlistmaker.search.ui.activity

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.search.domain.Track
import com.example.playlistmaker.search.ui.Content
import com.example.playlistmaker.search.ui.Router
import com.example.playlistmaker.search.ui.SearchScreenState
import com.example.playlistmaker.search.ui.adapter.TracksAdapter
import com.example.playlistmaker.search.ui.view_model.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private val viewModel by viewModel<SearchViewModel>()
    private lateinit var router: Router

    private val searchAdapter = TracksAdapter {
        clickOnTrack(it)
    }

    private val historyAdapter = TracksAdapter {
        clickOnTrack(it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.apply {

            observeState().observe(this@SearchActivity) {
                render(it)
            }

            observeShowToast().observe(this@SearchActivity) {
                showToast(it)
            }

        }

        initToolbar()

        initInput()

        initSearchResults()

        initHistory()

        router = Router(this)

    } //конец onCreate!

    private fun render(state: SearchScreenState) {
        when (state) {
            is SearchScreenState.Success -> {
                searchAdapter.tracks = state.tracks
                showContent(Content.SEARCH_RESULT)
            }
            is SearchScreenState.ShowHistory -> {
                historyAdapter.tracks = state.tracks
                showContent(Content.TRACKS_HISTORY)
            }
            is SearchScreenState.Error -> {
                showContent(Content.ERROR)
            }
            is SearchScreenState.NothingFound -> showContent(Content.NOT_FOUND)
            is SearchScreenState.Loading -> showContent(Content.LOADING)

        }
    }

    private fun showToast(additionalMessage: String) {
        Toast.makeText(this, additionalMessage, Toast.LENGTH_LONG).show()
    }

    private fun initToolbar() {
        binding.searchToolbar.setNavigationOnClickListener {
            router.goBack()
        }
    }

    private fun initInput() {

        binding.searchSongEt.doOnTextChanged { s: CharSequence?, _, _, _ ->
            binding.clearSearchBtn.visibility = clearButtonVisibility(s)
            if (binding.searchSongEt.hasFocus() && s.toString().isNotEmpty()) {
                showContent(Content.SEARCH_RESULT)
            }
            viewModel.searchDebounce(binding.searchSongEt.text.toString())
        }

        binding.searchSongEt.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.getTracks(binding.searchSongEt.text.toString())
            }
            false
        }

        binding.tryAgainBtn.setOnClickListener {
            viewModel.getTracks(binding.searchSongEt.text.toString())
        }

        binding.clearSearchBtn.visibility =
            clearButtonVisibility(binding.searchSongEt.text)

        binding.searchSongEt.requestFocus()

        binding.clearSearchBtn.setOnClickListener {
            clearSearch()
        }
    }

    private fun clearSearch() {
        searchAdapter.tracks = arrayListOf()
        binding.searchSongEt.setText("")
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
        viewModel.clearSearch()

    }

    private fun initSearchResults() {
        binding.trackRv.adapter = searchAdapter
    }

    private fun initHistory() {
        binding.trackHistoryRv.adapter = historyAdapter
        binding.clearHistoryBtn.setOnClickListener {
            viewModel.clearHistory()
        }
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun clickOnTrack(track: Track) {
        if (viewModel.trackIsClickable.value == false) return
        viewModel.onSearchClicked(track)
        router.openAudioPlayer(track)
    }


    private fun showContent(content: Content) {
        when (content) {
            Content.NOT_FOUND -> {
                binding.trackRv.isVisible = false
                binding.networkProblemsLl.isVisible = false
                binding.trackSearchHistoryLl.isVisible = false
                binding.progressBar.isVisible = false
                binding.nothingToShowLl.isVisible = true
            }

            Content.ERROR -> {
                binding.trackRv.isVisible = false
                binding.networkProblemsLl.isVisible = true
                binding.trackSearchHistoryLl.isVisible = false
                binding.progressBar.isVisible = false
                binding.nothingToShowLl.isVisible = false
            }

            Content.TRACKS_HISTORY -> {
                historyAdapter.notifyDataSetChanged()
                binding.trackRv.isVisible = false
                binding.networkProblemsLl.isVisible = false
                binding.trackSearchHistoryLl.isVisible = true
                binding.progressBar.isVisible = false
                binding.nothingToShowLl.isVisible = false
            }

            Content.SEARCH_RESULT -> {
                searchAdapter.notifyDataSetChanged()
                binding.trackRv.isVisible = true
                binding.networkProblemsLl.isVisible = false
                binding.trackSearchHistoryLl.isVisible = false
                binding.progressBar.isVisible = false
                binding.nothingToShowLl.isVisible = false
            }

            Content.LOADING -> {
                binding.trackRv.isVisible = false
                binding.networkProblemsLl.isVisible = false
                binding.trackSearchHistoryLl.isVisible = false
                binding.progressBar.isVisible = true
                binding.nothingToShowLl.isVisible = false
            }
        }
    }

}
