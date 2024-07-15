package com.example.playlistmaker.media_library.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.BottomSheetViewBinding
import com.example.playlistmaker.new_playlist.domain.model.Playlist

class BottomSheetAdapter(private val clickListener: PlaylistClickListener) :
    RecyclerView.Adapter<BottomSheetViewHolder>() {

    val list = ArrayList<Playlist>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BottomSheetViewHolder {
        return BottomSheetViewHolder(
            BottomSheetViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: BottomSheetViewHolder, position: Int) {
        val playlistItem = list[holder.adapterPosition]
        holder.bind(playlistItem)
        holder.itemView.setOnClickListener { clickListener.onPlaylistClick(playlistItem) }
    }

    fun interface PlaylistClickListener {
        fun onPlaylistClick(playlist: Playlist)
    }
}