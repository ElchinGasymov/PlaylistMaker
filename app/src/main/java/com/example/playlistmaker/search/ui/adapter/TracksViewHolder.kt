package com.example.playlistmaker.search.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.TrackViewBinding
import com.example.playlistmaker.search.domain.Track

class TracksViewHolder(
    private val binding: TrackViewBinding,
    private val clickListener: (Track) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(
        model: Track,
        onLongClick: ((Track) -> Unit)?
    ) {

        val roundedCornersRadius =
            itemView.context.resources.getInteger(R.integer.glide_corner_radius_search)

        binding.trackNameTv.text = model.trackName
        binding.artistNameTv.text = model.artistName
        binding.trackTimeTv.text = model.trackTime
        Glide.with(itemView)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.ic_placeholder)
            .fitCenter()
            .transform(RoundedCorners(roundedCornersRadius))
            .into(binding.trackIv)

        itemView.setOnClickListener { clickListener(model) }

        itemView.setOnLongClickListener {
            onLongClick?.invoke(model)
            true
        }
    }

}
