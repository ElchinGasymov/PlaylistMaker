package com.example.playlistmaker.search.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.search.domain.Track
import com.example.playlistmaker.search.ui.Content
import com.example.playlistmaker.search.ui.SearchScreenState
import com.example.playlistmaker.search.ui.adapter.TracksAdapter
import com.example.playlistmaker.search.ui.view_model.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private val binding: FragmentSearchBinding by viewBinding(CreateMethod.INFLATE)
    private val viewModel by viewModel<SearchViewModel>()

    private val searchAdapter = TracksAdapter(
        clickListener = { clickOnTrack(it) }
    )

    private val historyAdapter = TracksAdapter(
        clickListener = { clickOnTrack(it) }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.apply {

            observeState().observe(viewLifecycleOwner) {
                render(it)
            }

            observeShowToast().observe(viewLifecycleOwner) {
                showToast(it)
            }

        }

        initInput()
        initSearchResults()
        initHistory()
    }

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
        Toast.makeText(requireContext(), additionalMessage, Toast.LENGTH_LONG).show()
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
        val view = activity?.currentFocus
        if (view != null) {
            val imm =
                activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
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

        val action = SearchFragmentDirections.actionSearchFragmentToPlayerFragment(track)
        findNavController().navigate(action)

    }


    private fun showContent(content: Content) {
        when (content) {
            Content.NOT_FOUND -> {
                binding.trackRv.isVisible = false
                binding.networkProblemsLl.isVisible = false
                binding.trackSearchHistoryLl.isVisible = false
                binding.progressBar.isVisible = false
                binding.nothingToShowLl.isVisible = true
                binding.clearHistoryBtn.isVisible = false
            }

            Content.ERROR -> {
                binding.trackRv.isVisible = false
                binding.networkProblemsLl.isVisible = true
                binding.trackSearchHistoryLl.isVisible = false
                binding.progressBar.isVisible = false
                binding.nothingToShowLl.isVisible = false
                binding.clearHistoryBtn.isVisible = false
            }

            Content.TRACKS_HISTORY -> {
                historyAdapter.notifyDataSetChanged()
                binding.trackRv.isVisible = false
                binding.networkProblemsLl.isVisible = false
                binding.trackSearchHistoryLl.isVisible = true
                binding.progressBar.isVisible = false
                binding.nothingToShowLl.isVisible = false
                binding.clearHistoryBtn.isVisible = true
            }

            Content.SEARCH_RESULT -> {
                searchAdapter.notifyDataSetChanged()
                binding.trackRv.isVisible = true
                binding.networkProblemsLl.isVisible = false
                binding.trackSearchHistoryLl.isVisible = false
                binding.progressBar.isVisible = false
                binding.nothingToShowLl.isVisible = false
                binding.clearHistoryBtn.isVisible = false
            }

            Content.LOADING -> {
                binding.trackRv.isVisible = false
                binding.networkProblemsLl.isVisible = false
                binding.trackSearchHistoryLl.isVisible = false
                binding.progressBar.isVisible = true
                binding.nothingToShowLl.isVisible = false
                binding.clearHistoryBtn.isVisible = false
            }
        }
    }
}