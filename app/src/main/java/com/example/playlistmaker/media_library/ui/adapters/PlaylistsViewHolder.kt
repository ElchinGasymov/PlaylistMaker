package com.example.playlistmaker.media_library.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ItemPlaylistBinding
import com.example.playlistmaker.new_playlist.domain.model.Playlist
import com.example.playlistmaker.utils.setImage

class PlaylistsViewHolder(
    private val binding: ItemPlaylistBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(model: Playlist) {
        val cornerRadius = itemView.resources.getDimensionPixelSize(R.dimen.corner_radius_8)

        binding.namePlaylist.text = model.playlistName
        binding.countTracks.text = itemView.resources.getQuantityString(R.plurals.tracks, model.tracksCount, model.tracksCount)

        binding.coverPlaylist.setImage(
            url = model.coverImageUrl,
            placeholder = R.drawable.ic_placeholder_512,
            cornerRadius = cornerRadius,
        )
    }
}