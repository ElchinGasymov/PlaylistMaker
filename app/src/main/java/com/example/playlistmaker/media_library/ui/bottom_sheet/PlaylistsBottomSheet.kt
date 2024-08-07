package com.example.playlistmaker.media_library.ui.bottom_sheet

import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.playlistmaker.databinding.BottomSheetBinding
import com.example.playlistmaker.media_library.ui.adapters.BottomSheetAdapter
import com.example.playlistmaker.media_library.ui.view_model.BottomSheetState
import com.example.playlistmaker.media_library.ui.view_model.BottomSheetViewModel
import com.example.playlistmaker.new_playlist.domain.model.Playlist
import com.example.playlistmaker.search.domain.Track
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.google.android.material.R as AndroidMaterialR

class PlaylistsBottomSheet : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetBinding
    private val viewModel by viewModel<BottomSheetViewModel>()

    private lateinit var playlistsAdapter: BottomSheetAdapter
    private lateinit var track: Track
    private val args: PlaylistsBottomSheetArgs by navArgs()

    override fun onStart() {
        super.onStart()
        setupRatio(requireContext(), dialog as BottomSheetDialog, 100)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        track = args.track

        initAdapter()
        initBtnCreate()
        initObserver()

    }

    private fun initAdapter() {
        playlistsAdapter = BottomSheetAdapter { playlist ->
            viewModel.onPlaylistClicked(playlist, track)
        }
        binding.playlistsRecycler.adapter = playlistsAdapter
    }

    private fun initBtnCreate() {
        binding.createPlaylistBtn.setOnClickListener {
            val action = PlaylistsBottomSheetDirections.actionBottomSheetToNewPlaylist()
            findNavController().navigate(
                action
            )
        }
    }

    private fun initObserver() {
        viewLifecycleOwner.lifecycle.coroutineScope.launch {
            viewModel.contentFlow.collect { screenState ->
                render(screenState)
            }
        }
    }

    private fun render(state: BottomSheetState) {
        when (state) {
            is BottomSheetState.AddedAlready -> {
                val message =
                    getString(com.example.playlistmaker.R.string.already_added) + " \"" + state.playlistModel.playlistName + "\" "
                Toast
                    .makeText(requireContext(), message, Toast.LENGTH_SHORT)
                    .show()
            }

            is BottomSheetState.AddedNow -> {
                val message =
                    getString(com.example.playlistmaker.R.string.added) + " \"" + state.playlistModel.playlistName + "\" "

                Toast
                    .makeText(requireContext(), message, Toast.LENGTH_SHORT)
                    .show()
                dialog?.cancel()
            }

            else -> showContent(state.content)
        }
    }

    private fun showContent(content: List<Playlist>) {
        binding.playlistsRecycler.visibility = View.VISIBLE
        playlistsAdapter.apply {
            list.clear()
            list.addAll(content)
            notifyDataSetChanged()
        }
    }

    private fun setupRatio(context: Context, bottomSheetDialog: BottomSheetDialog, percetage: Int) {

        val bottomSheet =
            bottomSheetDialog.findViewById<View>(AndroidMaterialR.id.design_bottom_sheet) as FrameLayout
        val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(bottomSheet)
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = getBottomSheetDialogDefaultHeight(context, percetage)
        bottomSheet.layoutParams = layoutParams
        behavior.state = BottomSheetBehavior.STATE_COLLAPSED

    }

    private fun getBottomSheetDialogDefaultHeight(context: Context, percetage: Int): Int {
        return getWindowHeight(context) * percetage / 100
    }

    private fun getWindowHeight(context: Context): Int {
        val displayMetrics = DisplayMetrics()

        @Suppress("DEPRECATION") requireActivity().windowManager.defaultDisplay.getMetrics(
            displayMetrics
        )

        return displayMetrics.heightPixels
    }

}