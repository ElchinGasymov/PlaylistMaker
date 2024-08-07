package com.example.playlistmaker.player.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlayerBinding
import com.example.playlistmaker.player.ui.PlayerScreenState
import com.example.playlistmaker.player.ui.view_model.AudioPlayerViewModel
import com.example.playlistmaker.search.domain.Track
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerFragment : Fragment() {

    private val binding: FragmentPlayerBinding by viewBinding(CreateMethod.INFLATE)
    private val viewModel by viewModel<AudioPlayerViewModel>()
    private val args: PlayerFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val track = args.track

        viewModel.isTrackLiked(track.id)

        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

        viewModel.observeFavoriteState().observe(viewLifecycleOwner) {
            renderLikeButton(it)
        }

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        showTrack(track)

        viewModel.preparePlayer(track.previewUrl)

        binding.playIv.setOnClickListener {
            viewModel.playbackControl()
        }

        viewModel.initLikeButton(track.isFavorite)

        binding.likeIv.setOnClickListener {
            viewModel.onFavoriteClicked(track)
        }

        initAddToPlaylistButton(track)
    }

    private fun render(state: PlayerScreenState) {
        when (state) {
            is PlayerScreenState.Preparing -> {
            }

            is PlayerScreenState.Stopped -> {
                binding.playIv.setImageResource(R.drawable.ic_play)
                binding.currentProgressTimeTv.setText(R.string._00_00)
            }

            is PlayerScreenState.Paused -> {
                binding.playIv.setImageResource(R.drawable.ic_play)
            }

            is PlayerScreenState.Playing -> {
                binding.playIv.setImageResource(R.drawable.ic_pause)
            }

            is PlayerScreenState.UpdatePlayingTime -> {
                binding.currentProgressTimeTv.text = state.playingTime
            }
        }
    }

    private fun showTrack(track: Track) {

        binding.apply {
            Glide
                .with(coverIv)
                .load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
                .placeholder(R.drawable.ic_player_placeholder)
                .centerCrop()
                .transform(
                    RoundedCorners(
                        resources.getDimensionPixelSize(
                            R.dimen.corner_radius_8
                        )
                    )
                )
                .into(coverIv)

            trackName.text = track.trackName
            trackName.isSelected = true
            artistName.text = track.artistName
            trackName.isSelected = true
            genreValueTv.text = track.primaryGenreName
            countryValueTv.text = track.country
            durationValueTv.text = track.trackTime

            val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(track.releaseDate)
            if (date != null) {
                val formattedDatesString =
                    SimpleDateFormat("yyyy", Locale.getDefault()).format(date)
                yearValueTv.text = formattedDatesString
            }

            if (track.collectionName.isNotEmpty()) {
                albumValueTv.text = track.collectionName
            } else {
                albumValueTv.visibility = View.GONE
                albumTv.visibility = View.GONE
            }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    private fun renderLikeButton(isFavorite: Boolean) {
        val imageResource = if (isFavorite) {
            R.drawable.ic_is_liked
        } else {
            R.drawable.ic_is_not_liked
        }
        binding.likeIv.setImageResource(imageResource)
    }

    private fun initAddToPlaylistButton(track: Track) {
        binding.addToPlaylist.setOnClickListener { button ->
            (button as? ImageView)?.let { startAnimation(it) }
            val action = PlayerFragmentDirections.actionPlayerFragmentToBottomSheet(track)
            findNavController().navigate(action)
        }
    }

    private fun startAnimation(button: ImageView) {
        button.startAnimation(
            AnimationUtils.loadAnimation(
                requireContext(),
                R.anim.scale
            )
        )
    }

}