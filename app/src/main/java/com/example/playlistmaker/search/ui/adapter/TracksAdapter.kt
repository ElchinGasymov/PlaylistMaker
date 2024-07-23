package com.example.playlistmaker.search.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.TrackViewBinding
import com.example.playlistmaker.search.domain.Track

class TracksAdapter(
    private val clickListener: (Track) -> Unit,
    private val onLongClick: ((Track) -> Unit)? = null
) : RecyclerView.Adapter<TracksViewHolder>() {

    var tracks: List<Track> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksViewHolder {
        val binding = TrackViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TracksViewHolder(binding, clickListener)
    }

    override fun onBindViewHolder(holder: TracksViewHolder, position: Int) {
        holder.bind(model = tracks[position], onLongClick = onLongClick)
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

}
