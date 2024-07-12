package com.example.playlistmaker.media_library.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.media_library.ui.adapters.PlaylistsAdapter
import com.example.playlistmaker.media_library.ui.view_model.PlaylistsScreenState
import com.example.playlistmaker.media_library.ui.view_model.PlaylistsViewModel
import com.example.playlistmaker.new_playlist.domain.model.Playlist
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {

    private val binding: FragmentPlaylistsBinding by viewBinding(CreateMethod.INFLATE)
    private val viewModel by viewModel<PlaylistsViewModel>()
    private val playlistsAdapter = PlaylistsAdapter {
        clickOnPlaylist()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.contentFlow.collect { screenState ->
                render(screenState)
            }
        }

        binding.newPlaylistBtn.setOnClickListener {
            val action = LibraryFragmentDirections.actionLibraryFragmentToNewPlayListFragment()
            findNavController().navigate(action)
        }
    }

    private fun render(state: PlaylistsScreenState) {
        when (state) {
            is PlaylistsScreenState.Content -> showContent(state.playlists)
            PlaylistsScreenState.Empty -> showPlaceholder()
        }
    }

    private fun showPlaceholder() {
        binding.apply {
            nothingToShowIV.isVisible = true
            nothingToShowTV.isVisible = true
            PlaylistRv.isVisible = false
        }

    }

    private fun showContent(content: List<Playlist>) {

        binding.apply {
            nothingToShowIV.isVisible = false
            nothingToShowTV.isVisible = false
            PlaylistRv.isVisible = true
        }

        playlistsAdapter.apply {
            playlists.clear()
            playlists.addAll(content)
            notifyDataSetChanged()
        }
    }

    private fun initAdapter() {
        binding.PlaylistRv.adapter = playlistsAdapter
        //binding.PlaylistRv.addItemDecoration(PlaylistsOffsetItemDecoration(requireContext()))
    }

    private fun clickOnPlaylist() {
        if (!viewModel.isClickable) return
        viewModel.onPlaylistClick()
        Toast
            .makeText(requireContext(), "Clicked", Toast.LENGTH_SHORT)
            .show()
    }

    companion object {
        fun newInstance() = PlaylistsFragment()
    }

}
