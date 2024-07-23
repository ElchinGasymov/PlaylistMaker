package com.example.playlistmaker.playlist.presentation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.new_playlist.domain.model.Playlist
import com.example.playlistmaker.search.domain.Track
import com.example.playlistmaker.search.ui.adapter.TracksAdapter
import com.example.playlistmaker.utils.setImage
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment : Fragment() {
    private val viewModel by viewModel<PlaylistViewModel>()
    private val binding: FragmentPlaylistBinding by viewBinding(CreateMethod.INFLATE)
    private val args: PlaylistFragmentArgs by navArgs()
    private val tracksAdapter = TracksAdapter(
        clickListener = { clickOnTrack(it) },
        onLongClick = { track -> showConfirmTrackDeleteDialog(track, viewModel.playlist?.value) }
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
        viewModel.getPlaylist(args.playlist.id ?: 0)
        viewModel.playlist?.observe(viewLifecycleOwner) { playlist ->
            Log.d("TAG123", "onViewCreated: ")
            if (playlist != null) {
                with(binding) {
                    playlist?.coverImageUrl?.let {
                        ivPreview.setImage(
                            it,
                            R.drawable.ic_player_placeholder,
                            resources.getDimensionPixelSize(
                                R.dimen.corner_radius_8
                            )
                        )
                    }

                    tvTitle.text = playlist?.playlistName
                    tvDescription.text = playlist?.playlistDescription
                    tvDurationAndCount.text = getDurationAndCount(playlist!!)

                    tvEmptyTracks.isGone = playlist!!.tracksCount > 0
                    rvTracks.isVisible = playlist!!.tracksCount > 0
                    tracksAdapter.tracks = playlist!!.trackList
                    rvTracks.adapter = tracksAdapter
                    rvTracks.layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

                    ivShare.setOnClickListener {
                        onShareClick(playlist)
                    }

                    toolbar.setNavigationOnClickListener {
                        findNavController().navigateUp()
                    }

                    ivMenu.setOnClickListener {
                        val action =
                            PlaylistFragmentDirections.actionPlaylistFragmentToPlaylistMenuFragment(
                                playlist
                            )
                        findNavController().navigate(action)
                    }
                }
            }
        }
    }

    private fun onShareClick(playlist: Playlist?) {
        Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, playlist?.getShareText())
            type = "text/plain"
            startActivity(Intent.createChooser(this, null))
        }
    }

    private fun getDurationAndCount(playlist: Playlist): String {
        return context?.getString(
            R.string.playlist_count_and_duration,
            "0 минут",
            playlist.tracksCount.toString(),
        ).orEmpty()
    }

    private fun clickOnTrack(track: Track) {
        val action = PlaylistFragmentDirections.actionPlaylistFragmentToPlayerFragment(track)
        findNavController().navigate(action)
    }

    private fun showConfirmTrackDeleteDialog(track: Track, playlist: Playlist?) {
        val theme = if (viewModel.isDarkModeOn()) {
            R.style.CustomAlertDialogTheme_Dark
        } else {
            R.style.CustomAlertDialogTheme_Light
        }

        MaterialAlertDialogBuilder(requireContext(), theme)
            .setTitle(getString(R.string.playlist_delete_track_title))
            .setNeutralButton(getString(R.string.no)) { _, _ -> }
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                deleteTrack(
                    track,
                    playlist
                )
            }
            .show()
    }


    private fun deleteTrack(track: Track, playlist: Playlist?) {
        if (playlist != null) {
            viewModel.deleteTrack(track.id, playlist)
        }
    }
}
