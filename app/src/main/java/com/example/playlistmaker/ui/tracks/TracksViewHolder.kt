package com.example.playlistmaker.ui.tracks

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.model.Track

class TracksViewHolder(itemView: View, private val clickListener: (Track) -> Unit) : RecyclerView.ViewHolder(itemView) {

    private val trackName: TextView = itemView.findViewById(R.id.trackNameTv)
    private val artistName: TextView = itemView.findViewById(R.id.artistNameTv)
    private val trackTime: TextView = itemView.findViewById(R.id.trackTimeTv)

    private val imageView: ImageView = itemView.findViewById(R.id.trackIv)

    fun bind(model: Track) {

        val roundedCornersRadius = itemView.context.resources.getInteger(R.integer.glide_corner_radius_search)

        trackName.text = model.trackName
        artistName.text = model.artistName
        trackTime.text = model.trackTime
        Glide.with(itemView)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.ic_placeholder)
            .fitCenter()
            .transform(RoundedCorners(roundedCornersRadius))
            .into(imageView)

        itemView.setOnClickListener { clickListener(model) }
    }

}
