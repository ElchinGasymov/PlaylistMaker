package com.example.playlistmaker.media_library.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.BottomSheetViewBinding
import com.example.playlistmaker.new_playlist.domain.model.Playlist
import com.example.playlistmaker.utils.setImage

class BottomSheetViewHolder(
    private val binding: BottomSheetViewBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(model: Playlist) {
        val cornerRadius = itemView.resources.getDimensionPixelSize(R.dimen._2dp)

        binding.namePlaylist.text = model.playlistName
        binding.countTracks.text = itemView.resources.getQuantityString(R.plurals.tracks, model.tracksCount, model.tracksCount)

        binding.coverPlaylist.setImage(
            url = model.coverImageUrl,
            placeholder = R.drawable.ic_placeholder_512,
            cornerRadius = cornerRadius,
        )
    }
}