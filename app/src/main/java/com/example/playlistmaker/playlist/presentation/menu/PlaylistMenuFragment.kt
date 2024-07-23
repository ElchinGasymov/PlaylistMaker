package com.example.playlistmaker.playlist.presentation.menu

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistMenuBinding
import com.example.playlistmaker.new_playlist.domain.PlaylistCreationMode
import com.example.playlistmaker.new_playlist.domain.model.Playlist
import com.example.playlistmaker.utils.setImage
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistMenuFragment : BottomSheetDialogFragment() {
    private val binding: FragmentPlaylistMenuBinding by viewBinding(CreateMethod.INFLATE)
    private val args: PlaylistMenuFragmentArgs by navArgs()
    private val viewModel by viewModel<PlaylistMenuViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val playlist = args.playlist
        with(binding) {
            playlist.coverImageUrl.let {
                playlistPreview.ivPreview.setImage(
                    url = it,
                    placeholder = R.drawable.ic_player_placeholder,
                    cornerRadius = resources.getDimensionPixelSize(
                        R.dimen.corner_radius_8
                    )
                )
            }

            playlistPreview.tvTitle.text = playlist.playlistName
            playlistPreview.tvDescription.text = playlist.tracksCount.toString()

            tvEditPlaylist.setOnClickListener {
                val action =
                    PlaylistMenuFragmentDirections.actionPlaylistMenuFragmentToNewPlayListFragment(
                        playlist = playlist,
                        mode = PlaylistCreationMode.EDIT
                    )
                findNavController().navigate(action)
            }

            tvSharePlaylist.setOnClickListener {
                Intent(Intent.ACTION_SEND).apply {
                    putExtra(Intent.EXTRA_TEXT, playlist.getShareText())
                    type = "text/plain"
                    startActivity(Intent.createChooser(this, null))
                }
            }

            tvDeletePlaylist.setOnClickListener {
                showConfirmPlaylistDeleteDialog(playlist)
            }
        }
    }

    private fun showConfirmPlaylistDeleteDialog(playlist: Playlist) {
        val theme = if (viewModel.isDarkModeOn()) {
            R.style.CustomAlertDialogTheme_Dark
        } else {
            R.style.CustomAlertDialogTheme_Light
        }

        MaterialAlertDialogBuilder(requireContext(), theme)
            .setTitle(getString(R.string.playlist_delete_title))
            .setNeutralButton(getString(R.string.no)) { _, _ -> }
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                viewModel.deletePlaylist(playlist)
                findNavController().popBackStack(R.id.playlistFragment, true)
            }
            .show()
    }
}