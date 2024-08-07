package com.example.playlistmaker.media_library.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.ItemPlaylistBinding
import com.example.playlistmaker.new_playlist.domain.model.Playlist

class PlaylistsAdapter(private val clickListener: PlaylistClickListener) :
    RecyclerView.Adapter<PlaylistsViewHolder>() {

    val playlists = ArrayList<Playlist>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistsViewHolder {
        return PlaylistsViewHolder(
            ItemPlaylistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount() = playlists.size

    override fun onBindViewHolder(holder: PlaylistsViewHolder, position: Int) {
        val playlistItem = playlists[holder.adapterPosition]
        holder.bind(playlistItem)
        holder.itemView.setOnClickListener { clickListener.onTrackClick(playlistItem) }
    }

    fun interface PlaylistClickListener {
        fun onTrackClick(playlist: Playlist)
    }
}