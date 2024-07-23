package com.example.playlistmaker.media_library.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.playlistmaker.databinding.FragmentFavouriteTracksBinding
import com.example.playlistmaker.media_library.ui.FavoritesState
import com.example.playlistmaker.media_library.ui.view_model.FavouriteTracksViewModel
import com.example.playlistmaker.search.domain.Track
import com.example.playlistmaker.search.ui.adapter.TracksAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavouriteTracksFragment : Fragment() {

    private val binding: FragmentFavouriteTracksBinding by viewBinding(CreateMethod.INFLATE)
    private val viewModel by viewModel<FavouriteTracksViewModel>()
    private val tracksAdapter = TracksAdapter(
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

        viewModel.observeContentState().observe(viewLifecycleOwner) {
            render(it)
        }

        initAdapter()
        initLikedTracksList()
    }

    private fun clickOnTrack(track: Track) {
        if (!viewModel.isClickable) return
        viewModel.onTrackClick()

        val action = LibraryFragmentDirections.actionLibraryFragmentToPlayerFragment(track)
        findNavController().navigate(action)
    }

    private fun initAdapter() {
        binding.favouriteListRV.adapter = tracksAdapter
    }

    private fun render(state: FavoritesState) {
        when (state) {
            is FavoritesState.Empty -> {
                binding.favouriteListRV.isVisible = false
                binding.nothingToShowIV.isVisible = true
                binding.nothingToShowTV.isVisible = true
            }

            is FavoritesState.Content -> {
                tracksAdapter.notifyDataSetChanged()
                tracksAdapter.tracks = state.tracks
                binding.favouriteListRV.isVisible = true
                binding.nothingToShowIV.isVisible = false
                binding.nothingToShowTV.isVisible = false
            }
        }
    }

    private fun initLikedTracksList() {
        viewModel.getFavoritesTracks()
    }

    companion object {
        fun newInstance() = FavouriteTracksFragment()
    }

}
